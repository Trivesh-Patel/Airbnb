package com.project.airBnb.airbnbApp.repositories;

import com.project.airBnb.airbnbApp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByPaymentSessionId(String sessionId);
}
