package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Order;
import com.motorcycleparts.motorcycleparts_master.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(long customerId);

    @Query("SELECT o FROM Order o")
    List<Order> findAllOrders();


//    List<Order> findAllByOrderStatusStatus(Status status);
    Order findByOrderCode(String orderCode);
    Order findByCaptureId(String captureCode);

    Order findByOrderCodeAndOrderStatusId(String orderCode, long orderStatusId);

    // 1. Get all orders with captureId being null
    List<Order> findByCaptureIdIsNull();

    // 2. Get all orders with a specific status
    List<Order> findByOrderStatusStatus(Status status);

    List<Order> findByCustomerIdAndCaptureIdIsNotNull(Long customerId);

    List<Order> findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(long customerId, Status status);



}
