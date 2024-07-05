package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.OrderDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperOrderImple;
import com.motorcycleparts.motorcycleparts_master.model.Order;
import com.motorcycleparts.motorcycleparts_master.model.Status;
import com.motorcycleparts.motorcycleparts_master.model.Summary;
import com.motorcycleparts.motorcycleparts_master.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
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
            case "REFUNDED":
                orders = orderRepository.findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(customerId, Status.REFUNDED);
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

    @GetMapping("/orders-by-customer-name")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerName(@RequestParam(value = "name", required = false) String name) {
        List<Order> orders;
        System.out.println("NameCus: "+name);
        if (name == null || name.isEmpty()) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByCustomer_NameContaining(name);
        }
        return ResponseEntity.ok(orders.stream().map(mapperOrder::mapTo).collect(Collectors.toList()));
    }

    @GetMapping("/get-summary")
    public ResponseEntity<?> getSummary(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
        Double totalAmount;
        Long totalOrders;
        Long totalProducts;

        if (startDate != null && endDate == null) {
            endDate = new Date(); // Nếu không có ngày kết thúc, sử dụng ngày hiện tại
        } else if (startDate == null && endDate != null) {
            startDate = new Date(Long.MIN_VALUE); // Nếu không có ngày bắt đầu, sử dụng ngày nhỏ nhất có thể
        }

        if (startDate == null && endDate == null) {
            totalAmount = orderRepository.getTotalAmountByStatus(Status.DELIVERED);
            totalOrders = orderRepository.countByStatus(Status.DELIVERED);
            totalProducts = orderRepository.countDistinctProductsByStatus(Status.DELIVERED);
        } else {
            totalAmount = orderRepository.getTotalAmountByStatusAndDateRange(Status.DELIVERED, startDate, endDate);
            totalOrders = orderRepository.countByStatusAndDateRange(Status.DELIVERED, startDate, endDate);
            totalProducts = orderRepository.countDistinctProductsByStatusAndDateRange(Status.DELIVERED, startDate, endDate);
            System.out.println("totalAmount: "+totalProducts);
//            totalProducts = orderDetailRepository.getTotalQuantityForStatusAndDateRange(Status.DELIVERED, startDate, endDate);
        }

        return ResponseEntity.ok(new Summary(totalAmount, totalOrders, totalProducts));
    }


    @GetMapping("/get-revenue-by-year")
    public ResponseEntity<?> getRevenueByYear(@RequestParam("year") int year) {
        List<Object[]> results = orderRepository.getTotalAmountByYearAndStatus(year, Status.DELIVERED);

        // Khởi tạo một mảng gồm 12 phần tử, tất cả đều là 0.0
        Double[] revenueByMonth = new Double[12];
        Arrays.fill(revenueByMonth, 0.0);

        for (Object[] result : results) {
            int month = (int) result[0];
            double revenue = (double) result[1];
            revenueByMonth[month - 1] = revenue; // Lưu ý: Tháng trong Java bắt đầu từ 1, nhưng chỉ số mảng bắt đầu từ 0
        }

        return ResponseEntity.ok(revenueByMonth);
    }




}
