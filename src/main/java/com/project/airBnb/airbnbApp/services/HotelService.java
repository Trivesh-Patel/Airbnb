package com.project.airBnb.airbnbApp.services;

import com.project.airBnb.airbnbApp.dto.HotelDto;
import com.project.airBnb.airbnbApp.dto.HotelInfoDto;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

//    List<HotelDto> getAllHotels();

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long id);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
