package com.project.airBnb.airbnbApp.repositories;

import com.project.airBnb.airbnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}