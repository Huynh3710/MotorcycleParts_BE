package com.motorcycleparts.motorcycleparts_master.controller;

import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.service.SparePartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/get-api")
//@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class BrandPartsController {
    private final SparePartsService sparePartsService;
    @GetMapping("/get/byBrand/{id}")
    ResponseEntity<List<SparePartsDto>> getByBrand(@PathVariable Long id){
        return ResponseEntity.ok(sparePartsService.getSparePartsByBrandId(id));
    }
}
