package com.motorcycleparts.motorcycleparts_master.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long orderId;
    private Long sparePartsId;
    private Long customerId;
    private String content;
    private Integer rating;
}
