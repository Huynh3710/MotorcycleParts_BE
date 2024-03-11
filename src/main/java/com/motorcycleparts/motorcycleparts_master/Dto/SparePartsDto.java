package com.motorcycleparts.motorcycleparts_master.Dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SparePartsDto {

    private Long id;
    private String name;
    private Long sellNumber;
    private String description;
    private Double unitPrice;
    private String image;
    //số lượng còn lại
    private Long quantityRe;
    private float start;
    private SparePartsTypeDto sparePartsType;
    private BrandPartsDto brandPartsDto;

}
