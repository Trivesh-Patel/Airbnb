package com.project.airBnb.airbnbApp.controller;

import com.project.airBnb.airbnbApp.dto.BookingDto;
import com.project.airBnb.airbnbApp.dto.BookingRequestDto;
import com.project.airBnb.airbnbApp.dto.GuestDto;
import com.project.airBnb.airbnbApp.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

        private final BookingService bookingService;

        @PostMapping("/init")
        public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequestDto bookingRequestDto) {
            return ResponseEntity.ok(bookingService.initializeBooking(bookingRequestDto));
        }

        @PostMapping("/{bookingId}/addGuests")
        public  ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId,
                                                     @RequestBody List<GuestDto> guestDtos) {
            return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtos));
        }

    @PostMapping("/{bookingId}/payments")
    public  ResponseEntity<Map<String, String>> initiatePayments(@PathVariable Long bookingId) {
        String sessionUrl = bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
    }
}
