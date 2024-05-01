package com.motorcycleparts.motorcycleparts_master.Dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsDto {
    private Long id;
    private String content;
    private Integer rating;
    private Date date;
    private SparePartsDto spareParts;
    private CustomerDto customer;
    private List<ReviewsDto> childReviews;
}
