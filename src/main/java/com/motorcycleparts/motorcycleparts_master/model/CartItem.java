package com.motorcycleparts.motorcycleparts_master.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long cartId; 
    private Long sparePartId;
    private float price;
    private int quantity;
}