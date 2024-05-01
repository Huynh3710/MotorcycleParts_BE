package com.motorcycleparts.motorcycleparts_master.mapper;

import com.motorcycleparts.motorcycleparts_master.Dto.OrderDetailDto;
import com.motorcycleparts.motorcycleparts_master.model.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperOrderDetailImpl implements MapperDto<OrderDetail, OrderDetailDto>{
    private final ModelMapper modelMapper;
    @Override
    public OrderDetailDto mapTo(OrderDetail orderDetail) {
        return modelMapper.map(orderDetail, OrderDetailDto.class);
    }

    @Override
    public OrderDetail mapFrom(OrderDetailDto orderDetailDto) {
        return modelMapper.map(orderDetailDto, OrderDetail.class);
    }
}
