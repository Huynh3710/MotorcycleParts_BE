package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    public Reviews findBySparePartsIdAndCustomerId(Long sparePartsId, Long customerId);

    public List<Reviews> findAllBySparePartsId(Long sparePartsId);
}
