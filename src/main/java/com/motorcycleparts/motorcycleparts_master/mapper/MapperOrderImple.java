package com.motorcycleparts.motorcycleparts_master.mapper;

import com.motorcycleparts.motorcycleparts_master.Dto.OrderDto;
import com.motorcycleparts.motorcycleparts_master.model.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperOrderImple implements MapperDto<Order, OrderDto>{
    private final ModelMapper modelMapper;
    @Override
    public OrderDto mapTo(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public Order mapFrom(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }
}
