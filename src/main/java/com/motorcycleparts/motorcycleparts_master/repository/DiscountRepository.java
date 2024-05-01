package com.motorcycleparts.motorcycleparts_master.repository;


import com.motorcycleparts.motorcycleparts_master.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
