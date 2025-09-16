package com.project.airBnb.airbnbApp.controller;

import com.project.airBnb.airbnbApp.dto.HotelInfoDto;
import com.project.airBnb.airbnbApp.dto.HotelPriceDto;
import com.project.airBnb.airbnbApp.dto.HotelSearchRequest;
import com.project.airBnb.airbnbApp.services.HotelService;
import com.project.airBnb.airbnbApp.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {
        Page<HotelPriceDto> page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));

    }

}
