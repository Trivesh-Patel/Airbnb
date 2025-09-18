package com.project.airBnb.airbnbApp.services;

import com.project.airBnb.airbnbApp.dto.BookingDto;
import com.project.airBnb.airbnbApp.dto.BookingRequestDto;
import com.project.airBnb.airbnbApp.dto.GuestDto;
import com.stripe.model.Event;

import java.util.List;


public interface BookingService {
    BookingDto initializeBooking(BookingRequestDto bookingRequestDto);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtos);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}
