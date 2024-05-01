package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.provinces.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {
    //get districts by province code
    List<District> findDistrictByProvinceCode(String code);
    District findDistrictByCode(String code);
}
