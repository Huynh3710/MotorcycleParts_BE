package com.motorcycleparts.motorcycleparts_master.Dto;

import com.motorcycleparts.motorcycleparts_master.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private Date orderDate;
    private double amountPrice;
    private float shipping;
    private String orderCode;
    private Long customerId;
    private List<OrderDetailDto> orderDetails;
    private PaymentDto payment;
    private OrderStatusDto orderStatus;
    private String captureId;
    private Address address;
}
