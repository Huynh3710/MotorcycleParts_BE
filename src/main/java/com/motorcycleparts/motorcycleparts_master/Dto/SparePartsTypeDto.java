package com.motorcycleparts.motorcycleparts_master.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SparePartsTypeDto {
    private Long id;
    private String name;
    private String image;
}
