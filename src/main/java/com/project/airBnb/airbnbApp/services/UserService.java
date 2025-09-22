package com.project.airBnb.airbnbApp.services;

import com.project.airBnb.airbnbApp.dto.ProfileUpdateRequestDto;
import com.project.airBnb.airbnbApp.dto.UserDto;
import com.project.airBnb.airbnbApp.entity.User;

public interface UserService {

    User getUserById(Long userId);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
