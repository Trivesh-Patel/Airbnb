package com.project.airBnb.airbnbApp.dto;

import com.project.airBnb.airbnbApp.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;
    private Long userId;
    private String name;
    private Gender gender;
    private Integer age;
}
