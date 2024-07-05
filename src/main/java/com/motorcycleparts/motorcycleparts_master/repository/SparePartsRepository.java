package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.Discount;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SparePartsRepository extends JpaRepository<SpareParts,Long>, PagingAndSortingRepository<SpareParts, Long> {


//    Page<SpareParts> findAll(Pageable pageable);

    List<SpareParts> findSparePartsByNameContaining(String keyword);

    List<SpareParts> findSparePartsByBrandPartsIdAndSparePartsTypeId(Long brandId, Long typeId);
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
