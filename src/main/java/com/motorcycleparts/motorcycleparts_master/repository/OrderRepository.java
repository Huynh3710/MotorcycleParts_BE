package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Customer;
import com.motorcycleparts.motorcycleparts_master.model.Order;
import com.motorcycleparts.motorcycleparts_master.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(long customerId);

    @Query("SELECT o FROM Order o")
    List<Order> findAllOrders();

    List<Order> findByCustomer_NameContaining(String name);

    @Query("SELECT o FROM Order o WHERE o.customer.name = :name")
    List<Order> findOrdersByCustomerName(@Param("name") String name);

//    List<Order> findAllByOrderStatusStatus(Status status);
    Order findByOrderCode(String orderCode);
    Order findByCaptureId(String captureCode);

    Order findByOrderCodeAndOrderStatusId(String orderCode, long orderStatusId);

    List<Order> findByCustomer(Customer customer);

    List<Order> findAllByCustomerIdAndOrderStatusStatusOrderByOrderDateDesc(long customerId, Status status);

    @Query("SELECT SUM(o.amountPrice) FROM Order o WHERE o.orderStatus.status = :status")
    Double getTotalAmountByStatus(Status status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus.status = :status")
    Long countByStatus(Status status);

    @Query("SELECT COUNT(DISTINCT od.spareParts.id) FROM Order o JOIN o.orderDetails od WHERE o.orderStatus.status = :status")
    Long countDistinctProductsByStatus(Status status);
    @Query("SELECT SUM(o.amountPrice) FROM Order o WHERE o.orderStatus.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
Double getTotalAmountByStatusAndDateRange(Status status, Date startDate, Date endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
    Long countByStatusAndDateRange(Status status, Date startDate, Date endDate);

    @Query("SELECT COUNT(DISTINCT od.spareParts.id) FROM Order o JOIN o.orderDetails od WHERE o.orderStatus.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
    Long countDistinctProductsByStatusAndDateRange(Status status, Date startDate, Date endDate);

//    @Query("SELECT SUM(od.quantity) FROM Order o JOIN o.orderDetails od WHERE o.orderStatus.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
//    Long getTotalQuantityForStatusAndDateRange(Status status, Date startDate, Date endDate);

    @Query("SELECT MONTH(o.orderDate), SUM(o.amountPrice) FROM Order o WHERE YEAR(o.orderDate) = :year AND o.orderStatus.status = :status GROUP BY MONTH(o.orderDate)")
    List<Object[]> getTotalAmountByYearAndStatus(int year, Status status);


}
