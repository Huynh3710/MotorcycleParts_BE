package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.OrderDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperOrderImple;
import com.motorcycleparts.motorcycleparts_master.model.Order;
import com.motorcycleparts.motorcycleparts_master.model.Status;
import com.motorcycleparts.motorcycleparts_master.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;

    private final MapperOrderImple mapperOrder;

    @GetMapping("/get-all-orders-by-admin")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders.stream().map(mapperOrder::mapTo).collect(Collectors.toList()));
    }


    @GetMapping("/orders-by-customerId/{customerId}/{status}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByCustomerId(@PathVariable("customerId") Long customerId, @PathVariable("status") String status) {
        List<Order> orders;
        switch (status) {
            case "ALL":
                orders = orderRepository.findByCustomerId(customerId);
                break;
            case "PENDING":
                orders = orderRepository.findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(customerId, Status.PENDING);
                break;
            case "CAPTURED":
                orders = orderRepository.findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(customerId, Status.CAPTURED);
                break;
            case "PREPARING":
                orders = orderRepository.findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(customerId, Status.PREPARING);
                break;
            case "SHIPMENT":
                orders = orderRepository.findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(customerId, Status.SHIPMENT);
                break;
            case "DELIVERED":
                orders = orderRepository.findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(customerId, Status.DELIVERED);
                break;
            case "CANCELED":
                orders = orderRepository.findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(customerId, Status.CANCELLED);
                break;
            default:
                return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(orders.stream().map(mapperOrder::mapTo).collect(Collectors.toList()));
    }

    @PutMapping("request-cancle-by-customer/{orderId}")
    public ResponseEntity<?> requestCancleByCustomer(@PathVariable("orderId") Long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            return ResponseEntity.badRequest().body("Order not found");
        }
        order.setTempStatus(true);
        orderRepository.save(order);
        return ResponseEntity.ok("Request cancel success");
    }
}
