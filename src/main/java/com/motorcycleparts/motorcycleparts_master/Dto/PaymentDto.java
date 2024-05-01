package com.motorcycleparts.motorcycleparts_master.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class PaymentDto {
    private long id;
    private Date date;
    private String paymentMethod;
}