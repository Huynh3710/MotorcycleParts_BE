package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.provinces.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Long> {

    //get all province by code
    Province findProvinceByCode(String code);
    List<Province> findByShippingRateIsNotNull();

    Province findProvinceByFullName(String fullName);

    List<Province> findProvinceByFullNameContaining(String keyword);
}
