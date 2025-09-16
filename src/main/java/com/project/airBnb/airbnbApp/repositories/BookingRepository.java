package com.project.airBnb.airbnbApp.repositories;

import com.project.airBnb.airbnbApp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
