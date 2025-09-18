package com.project.airBnb.airbnbApp.services;

import com.project.airBnb.airbnbApp.dto.BookingDto;
import com.project.airBnb.airbnbApp.dto.BookingRequestDto;
import com.project.airBnb.airbnbApp.dto.GuestDto;
import com.project.airBnb.airbnbApp.entity.*;
import com.project.airBnb.airbnbApp.entity.enums.BookingStatus;
import com.project.airBnb.airbnbApp.exceptions.ResourceNotFoundException;
import com.project.airBnb.airbnbApp.exceptions.UnauthorisedException;
import com.project.airBnb.airbnbApp.repositories.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    private final CheckoutService checkoutService;
    private final PricingService pricingService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequestDto bookingRequestDto) {

        Hotel hotel  = hotelRepository.findById(bookingRequestDto.getHotelId()).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with id: "+bookingRequestDto.getHotelId()));

        Room room  = roomRepository.findById(bookingRequestDto.getHotelId()).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with id: "+bookingRequestDto.getHotelId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate(),bookingRequestDto.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate())+1;

        if(inventoryList.size()!= daysCount) {
            throw  new IllegalStateException("Room is not available anymore");
        }

        //RESERVE THE ROOM OR UPDATE THE BOOKED COUNT IN INVENTORIES

        inventoryRepository.initBooking(room.getId(), bookingRequestDto.getCheckInDate(),
                bookingRequestDto.getCheckOutDate(), bookingRequestDto.getRoomsCount());

        //DYNAMIC PRICING
        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequestDto.getRoomsCount()));

        //CREATE THE BOOKING
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .user(getCurrentUser())
                .amount(totalPrice)
                .roomsCount(bookingRequestDto.getRoomsCount())
                .build();

        booking=bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtos) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResourceNotFoundException("Booking not found with id: "+bookingId));
        User user = getCurrentUser();

        if(!user.getId().equals(booking.getUser().getId())) {
            throw new UnauthorisedException("Booking does not belong to this user with id: "+user.getId());
        }
        if(hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }
        if(booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking is not under Reservation state, cannot add guests");
        }

        for(GuestDto guestDto: guestDtos) {
            Guest guest = modelMapper.map(guestDto,Guest.class);
            guest.setUser(user);
            guest= guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking=bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);
    }

    @Override
    @Transactional
    public String initiatePayments(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with this id: "+ bookingId)
        );

        User user = getCurrentUser();

        if(!user.getId().equals(booking.getUser().getId())) {
            throw new UnauthorisedException("Booking does not belong to this user with id: "+user.getId());
        }
        if(hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }

        String sessionUrl = checkoutService.getCheckoutSession(booking,
                frontendUrl+"/payments/success", frontendUrl+"/payments/failure");
        booking.setBookingStatus(BookingStatus.PAYMENTS_PENDING);
        bookingRepository.save(booking);
        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {
        if("checkout.session.completed".equals(event.getType())){
            Session session = (Session)event.getDataObjectDeserializer().getObject().orElse(null);
            if(session == null) return;

            String sessionId = session.getId();
            Booking booking =
                    bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(
                            () -> new ResourceNotFoundException("Booking not found for session ID: "+sessionId)
                    );

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomsCount());

            inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomsCount());

            log.info("Successfully confirmed the booking for Booking ID: {}", booking.getId());

        }else{
            log.warn("Unhandled event type: {}", event.getType());
        }
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with this id: "+ bookingId)
        );

        User user = getCurrentUser();

        if(!user.getId().equals(booking.getUser().getId())) {
            throw new UnauthorisedException("Booking does not belong to this user with id: "+user.getId());
        }
        if(booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomsCount());

        inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomsCount());

        //Handle the refund
        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundCreateParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundCreateParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with this id: "+ bookingId)
        );

        User user = getCurrentUser();

        if(!user.getId().equals(booking.getUser().getId())) {
            throw new UnauthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        return booking.getBookingStatus().name();
    }

    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(30).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
