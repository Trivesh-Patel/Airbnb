package com.project.airBnb.airbnbApp.services;

import com.project.airBnb.airbnbApp.dto.HotelPriceDto;
import com.project.airBnb.airbnbApp.dto.HotelSearchRequest;
import com.project.airBnb.airbnbApp.dto.InventoryDto;
import com.project.airBnb.airbnbApp.dto.UpdateInventoryRequestDto;
import com.project.airBnb.airbnbApp.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
