package com.motorcycleparts.motorcycleparts_master.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Summary {
    private Double totalAmount;
    private Long totalOrders;
    private Long totalProducts;
}
