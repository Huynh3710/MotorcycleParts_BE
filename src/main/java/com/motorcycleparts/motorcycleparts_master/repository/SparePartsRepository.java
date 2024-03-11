package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SparePartsRepository extends JpaRepository<SpareParts,Long> {


//    List<SpareParts> findByBrandParts(Long brandId);
    Optional<SpareParts> findByName(String name);
    List<SpareParts> findSparePartsByBrandPartsId(Long id);

}
