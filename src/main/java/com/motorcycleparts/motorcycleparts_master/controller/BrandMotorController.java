package com.motorcycleparts.motorcycleparts_master.controller;

import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.model.BrandMotor;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.repository.BrandMotorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/get-api")
@RequiredArgsConstructor
public class BrandMotorController {
    final BrandMotorRepository brandMotorRepository;

    @GetMapping("/get-all-brand-motor")
    public ResponseEntity<List<BrandMotor>> getAllBrandMotor() {
        return ResponseEntity.ok(brandMotorRepository.findAll());
    }



}
