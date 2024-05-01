package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
