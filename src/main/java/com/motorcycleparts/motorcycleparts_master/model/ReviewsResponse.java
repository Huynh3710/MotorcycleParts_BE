package com.motorcycleparts.motorcycleparts_master.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewsResponse {
    private Float averageRating;
    private Integer totalReviews;
    private Integer fiveStar;
    private Integer fourStar;
    private Integer threeStar;
    private Integer twoStar;
    private Integer oneStar;
}
