package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.MotorType;
import com.motorcycleparts.motorcycleparts_master.model.Parts_MotorType;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Parts_MotorTypeRepository extends JpaRepository<Parts_MotorType, Long> {
    @Query("SELECT p.spareParts FROM Parts_MotorType p WHERE p.motorType.id = :motorTypeId")
    List<SpareParts> findAllSparePartsByMotorTypeId(@Param("motorTypeId") Long motorTypeId);

    @Query("SELECT p.motorType FROM Parts_MotorType p WHERE p.spareParts.id = :sparePartsId")
    List<MotorType> findAllMotorTypesBySparePartsId(@Param("sparePartsId") Long sparePartsId);

}
