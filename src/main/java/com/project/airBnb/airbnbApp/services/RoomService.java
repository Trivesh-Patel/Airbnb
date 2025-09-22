package com.project.airBnb.airbnbApp.services;

import com.project.airBnb.airbnbApp.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId,RoomDto roomDto);

    List<RoomDto> getAllRoomsInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);


    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
