package com.motorcycleparts.motorcycleparts_master.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutRequest {
    private long customerId;
    private String currency;
    private float amount;
    private List<Long> orderDetailsId;
}
