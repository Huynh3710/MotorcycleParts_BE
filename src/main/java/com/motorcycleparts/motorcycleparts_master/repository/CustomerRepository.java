package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Customer;
import com.motorcycleparts.motorcycleparts_master.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser(User user);
    Customer findByUserId(Long userId);

    List<Customer> findByNameContaining(String name);
    List<Customer> findByUser_IsLockedNotNullAndUser_CreationDateNotNull();
}
