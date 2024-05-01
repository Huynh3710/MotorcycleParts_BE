package com.motorcycleparts.motorcycleparts_master.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Long id;
    private int quantity;
    private Float price;
    private SparePartsDto spareParts;
}
