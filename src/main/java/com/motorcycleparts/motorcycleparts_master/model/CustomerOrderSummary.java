package com.motorcycleparts.motorcycleparts_master.model;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderSummary {
    private int totalOrders;
    private double totalAmount;
}
