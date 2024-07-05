package com.motorcycleparts.motorcycleparts_master.repository;

import com.motorcycleparts.motorcycleparts_master.model.MotorType;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MotorTypeRepository extends JpaRepository<MotorType, Long> {
//    @Query("SELECT p.spareParts FROM Parts_MotorType p WHERE p.motorType.id = :motorTypeId")
//    List<SpareParts> findAllSparePartsByMotorTypeId(@Param("motorTypeId") Long motorTypeId);

        MotorType findMotorTypeById(Long id);
        MotorType findByName(String name);

        Boolean existsByName(String name);

        List<MotorType> findByBrandMotor_Id(Long id);
        List<MotorType> findMotorTypeByNameContaining(String keyword);
}
