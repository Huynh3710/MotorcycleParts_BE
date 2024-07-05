package com.motorcycleparts.motorcycleparts_master.repository;


import com.motorcycleparts.motorcycleparts_master.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByIsActiveFalseOrEndDateAfter(LocalDateTime now);

    List<Discount> findDiscountByNameContainingOrCodeContaining(String name, String code);
}
