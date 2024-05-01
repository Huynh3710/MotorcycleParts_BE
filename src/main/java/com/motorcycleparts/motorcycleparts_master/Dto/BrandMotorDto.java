package com.motorcycleparts.motorcycleparts_master.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.motorcycleparts.motorcycleparts_master.model.MotorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

//Hãng xe máy (Honda, Yamaha, Suzuki, ...)
public class BrandMotorDto {
    private Long id;
    private String name;
    private String image;
    List<MotorTypeDto> motorTypes;
}