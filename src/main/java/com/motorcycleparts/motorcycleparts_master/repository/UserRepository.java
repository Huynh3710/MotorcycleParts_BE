package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

// lấy dữ liệu từ DB
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
