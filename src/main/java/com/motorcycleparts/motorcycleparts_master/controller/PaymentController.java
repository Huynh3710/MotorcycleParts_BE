package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.CheckOutRequest;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user-payment")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;

    @PostMapping("/cash-on-delivery")
    public Order cashOnDelivery(@RequestBody CheckOutRequest paymentRequest){
        Order order = new Order();
        OrderStatus orderStatus = OrderStatus.builder().status(Status.PENDING).orders(new ArrayList<>()).build();
        orderStatusRepository.save(orderStatus);

        Payment payment = Payment.builder()
                .date(new Date())
                .paymentMethod("cash-on-delivery")
                .build();
        paymentRepository.save(payment);

        Customer customer = customerRepository.findById(paymentRequest.getCustomerId()).orElse(null);
        orderRepository.save(order);
//        Address address = customer.getAddress().stream().filter(Address::isDefaultForShopping).findFirst().get();

        List<OrderDetail> orderDetailsList = new ArrayList<>();
        for (Long orderDetailId : paymentRequest.getOrderDetailsId()) {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new IllegalArgumentException("Order detail not found with id: " + orderDetailId));
            orderDetail.setOrder_(order);
            orderDetailsList.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        double Amount = orderDetailsList.stream().mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity()).sum();

//        order.setOrderCode(orderId);
        order.setAmountPrice(Amount);
        order.setOrderDate(new Date());
        order.setCustomer(customer);
//        order.setShipping(10000);
        order.setOrderStatus(orderStatus);
//        order.setAddress(address);
        order.setPayment(payment);
        order.setOrderDetails(orderDetailsList);
        orderRepository.save(order);

        return order;
    }

}


