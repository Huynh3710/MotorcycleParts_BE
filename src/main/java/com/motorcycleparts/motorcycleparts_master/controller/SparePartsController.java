package com.motorcycleparts.motorcycleparts_master.controller;


import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperSparePartsImpl;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import com.motorcycleparts.motorcycleparts_master.service.SparePartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/get-api")
//@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor

public class SparePartsController {

    @Autowired
    private MapperSparePartsImpl mapperSpareParts;
    @Autowired
    private final SparePartsService sparePartsService;
    @Autowired
    SparePartsRepository sparePartsRepository;
    @PutMapping("add-product")
    public ResponseEntity<SpareParts> addProduct (@RequestBody SpareParts spareParts){
        SpareParts newSpareParts = SpareParts.builder()
                                                .name(spareParts.getName())
                                                .unitPrice(spareParts.getUnitPrice())
                                                .build();
        return ResponseEntity.ok(newSpareParts);
    }
    @GetMapping("/get-all")
    ResponseEntity<List<SparePartsDto>> getAllP(){
        return new ResponseEntity<>(sparePartsService.getAllParts(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    ResponseEntity<SparePartsDto> getPId(@PathVariable Long id){
        System.out.println("id la: "+id);
        return ResponseEntity.ok(sparePartsService.getSparePartsById(id));
    }

}
