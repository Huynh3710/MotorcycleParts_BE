package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.SparePartsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SparePartsTypeRepository extends JpaRepository<SparePartsType,Long> {
    boolean existsByName(String name);
    List<SparePartsType> findSparePartsTypeByNameContaining(String keyword);
}
