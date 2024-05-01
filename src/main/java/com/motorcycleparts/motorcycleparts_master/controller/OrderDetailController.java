package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.OrderDetailDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperOrderDetailImpl;
import com.motorcycleparts.motorcycleparts_master.model.OrderDetail;
import com.motorcycleparts.motorcycleparts_master.repository.CartRepository;
import com.motorcycleparts.motorcycleparts_master.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailRepository orderDetailRepository;
    private final MapperOrderDetailImpl mapperOrderDetail;
    private final CartRepository cartRepository;

    //get orderdetail by selected cart
    @GetMapping("/order-detail-by-cart/{cartId}")
    public ResponseEntity<List<OrderDetailDto>> getOrderDetailByCart(@PathVariable("cartId") Long cartId,
                                                                     @RequestParam("listOrderDetailId") List<Long> listOrderDetailId) {
        System.out.println("listOrderDetailId = " + listOrderDetailId);
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByCartId(cartId);
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            if(listOrderDetailId.contains(orderDetail.getId())){
                OrderDetailDto orderDetailDto = mapperOrderDetail.mapTo(orderDetail);
                orderDetailDtos.add(orderDetailDto);
            }
        }
        return ResponseEntity.ok(orderDetailDtos);
    }



}
