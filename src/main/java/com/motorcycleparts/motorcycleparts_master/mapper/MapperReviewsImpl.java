package com.motorcycleparts.motorcycleparts_master.mapper;


import com.motorcycleparts.motorcycleparts_master.Dto.OrderDto;
import com.motorcycleparts.motorcycleparts_master.Dto.ReviewsDto;
import com.motorcycleparts.motorcycleparts_master.model.Order;
import com.motorcycleparts.motorcycleparts_master.model.Reviews;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperReviewsImpl implements MapperDto<Reviews, ReviewsDto>{
    private final ModelMapper modelMapper;
    @Override
    public ReviewsDto mapTo(Reviews reviews) {
        return modelMapper.map(reviews, ReviewsDto.class);
    }

    @Override
    public Reviews mapFrom(ReviewsDto reviewsDto) {
        return modelMapper.map(reviewsDto, Reviews.class);
    }
}
