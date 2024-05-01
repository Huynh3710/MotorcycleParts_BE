package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Discount;
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
//    List<SpareParts> findSparePartsByBrand
    List<SpareParts> findSparePartsBySparePartsTypeId(Long id);

    List<SpareParts> findSparePartsByStatus(String status);

    List<SpareParts> findSparePartsBySparePartsType_IdAndStatus(Long id, String status);

    List<SpareParts> findAllBySparePartsTypeId(Long typeId);

    List<SpareParts> findAllByDiscountId(Long discountId);
    List<SpareParts> findSparePartsBySparePartsTypeIdAndBrandPartsId(Long typeId, Long brandId);

}
