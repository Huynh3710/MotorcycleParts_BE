package com.motorcycleparts.motorcycleparts_master.service;

import com.motorcycleparts.motorcycleparts_master.Dto.CheckOutRequest;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SparePartsRepository sparePartsRepository;

    public void createOrder(CheckOutRequest paymentRequest, String orderId) {
        if(paymentRequest.getOrderDetailsId() == null || paymentRequest.getOrderDetailsId().isEmpty()){
            throw new RuntimeException("Order details are required");
        }
        if(paymentRequest.getCustomerId() == 0){
            throw new RuntimeException("Customer id is required");
        }
        Order order = new Order();
        orderRepository.save(order); // Lưu order ngay sau khi tạo
        OrderStatus orderStatus = OrderStatus.builder().status(Status.PENDING).orders(new ArrayList<>()).build();
        orderStatusRepository.save(orderStatus);
        Payment payment = Payment.builder()
                .date(new Date())
                .paymentMethod("paypal")
                .build();
        paymentRepository.save(payment);
        Customer customer = customerRepository.findById(paymentRequest.getCustomerId()).orElse(null);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }
        Optional<Address> optionalAddress = customer.getAddresses().stream().filter(Address::isDefault).findFirst();
        if (optionalAddress.isEmpty()) {
            System.out.println("Default address not found");
            return;
        }
        Address address = optionalAddress.get();

        List<OrderDetail> orderDetailsList = new ArrayList<>();
        for (Long orderDetailId : paymentRequest.getOrderDetailsId()) {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new IllegalArgumentException("Order detail not found with id: " + orderDetailId));
            SpareParts spareParts = orderDetail.getSpareParts();
            int quantity = orderDetail.getQuantity();
            System.out.println("quantity: " + quantity);
            // hàng tồn kho
            spareParts.setInventory(spareParts.getInventory() - quantity);
            spareParts.setSellNumber(spareParts.getSellNumber() + quantity);
            if (spareParts.getInventory() < 0) {
                throw new RuntimeException("Số lượng hàng tồn kho không đủ bạn chỉ có thể đặt tối đa "
                        + (spareParts.getInventory() + quantity) + " sản phẩm này");
            }
            if (spareParts.getInventory() == 0) {
                spareParts.setInventory(0);
                spareParts.setStatus("Hết hàng");
            }

            sparePartsRepository.save(spareParts);
            orderDetail.setOrder_(order);
            orderDetailsList.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        double Amount = orderDetailsList.stream().mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity()).sum();
        Amount = Math.round(Amount * 10.0) / 10.0;
        order.setOrderCode(orderId);
        order.setAmountPrice(Amount);
        order.setOrderDate(new Date());
        order.setCustomer(customer);
        order.setShipping(10000);
        order.setOrderStatus(orderStatus);
        order.setAddress(address);
        order.setPayment(payment);
        order.setOrderDetails(orderDetailsList);
        clearCartAfterPayment(order);
//        return order;
    }


    public void clearCartAfterPayment(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setCart(null);
            orderDetailRepository.save(orderDetail);
        }
    }


    public void captureOrder(String orderCode, String captureId){
        Order order = orderRepository.findByOrderCode(orderCode);
        order.getOrderStatus().setStatus(Status.CAPTURED);
        order.setCaptureId(captureId);
        orderRepository.save(order);
//        return order;
    }


    public Order refundOrder (String captureId){
        Order order = orderRepository.findByCaptureId(captureId);
        order.getOrderStatus().setStatus(Status.REFUNDED);
        orderRepository.save(order);
        return order;
    }

    public Order cancelOrder (String orderCode){
        Order order = orderRepository.findByOrderCode(orderCode);
        order.getOrderStatus().setStatus(Status.CANCELLED);
        orderRepository.save(order);
        return order;
    }


}
