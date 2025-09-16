package com.project.airBnb.airbnbApp.repositories;

import com.project.airBnb.airbnbApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
