package com.motorcycleparts.motorcycleparts_master.Dto;

import com.motorcycleparts.motorcycleparts_master.model.BrandMotor;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

//Tên xe máy (SH, Wave, Exciter, ...)
public class MotorTypeDto {
    private Long id;
    private String name;
    private String carYear;
    private String image;
    private BrandMotorDto brandMotor;
}