package com.motorcycleparts.motorcycleparts_master.repository;


import com.motorcycleparts.motorcycleparts_master.Dto.OrderDetailDto;
import com.motorcycleparts.motorcycleparts_master.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findOrderDetailByCartId(Long id);
}
