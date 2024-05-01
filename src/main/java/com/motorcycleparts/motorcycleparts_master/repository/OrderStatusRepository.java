package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

}
