package com.motorcycleparts.motorcycleparts_master.repository;


import com.motorcycleparts.motorcycleparts_master.Dto.OrderDetailDto;
import com.motorcycleparts.motorcycleparts_master.model.OrderDetail;
import com.motorcycleparts.motorcycleparts_master.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findOrderDetailByCartId(Long id);
    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.order_.orderStatus.status = :status AND od.order_.orderDate BETWEEN :startDate AND :endDate")
    Long getTotalQuantityForStatusAndDateRange(@Param("status") Status status, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
