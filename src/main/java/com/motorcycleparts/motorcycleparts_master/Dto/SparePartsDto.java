package com.motorcycleparts.motorcycleparts_master.Dto;

import com.motorcycleparts.motorcycleparts_master.model.Discount;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SparePartsDto {
    private Long id;
    private String name;
    private int sellNumber;
    private String description;
    private float unitPrice;
    private String image;
    private float weight;
    private String size;
    private float averageLifespan;
    private Float wattage;
    private Float voltage;
    private String type;
    private int year;
    private String origin;
    private int inventory;
    private float start;
    private Float warranty;
    private String iso;
    private String status;
    private String brandMotor;
    private SparePartsTypeDto sparePartsType;
    private BrandPartsDto brandParts;
    private DiscountDto discount;
}
