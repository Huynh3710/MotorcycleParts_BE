package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
