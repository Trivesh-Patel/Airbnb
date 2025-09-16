package com.project.airBnb.airbnbApp.repositories;

import com.project.airBnb.airbnbApp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
