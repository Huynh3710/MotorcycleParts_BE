package com.motorcycleparts.motorcycleparts_master.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//cart request
public class CartItem {
    private Long cartId; //=> cart item id also use as order-details id if it's returned to client
    private Long sparePartsId;
    private float price;
    private int quantity;
}
