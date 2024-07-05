package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandPartsRepository extends JpaRepository<BrandParts,Long> {
    Boolean existsByName(String name);
    List<BrandParts> findBrandPartsByNameContaining(String keyword);
}
