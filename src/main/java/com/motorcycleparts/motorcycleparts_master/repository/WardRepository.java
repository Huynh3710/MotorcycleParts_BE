package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.provinces.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WardRepository extends JpaRepository<Ward,Long> {
    //get wards by district code
    List<Ward> findWardByDistrictCode(String code);

    Ward findWardByCode(String code);
}
