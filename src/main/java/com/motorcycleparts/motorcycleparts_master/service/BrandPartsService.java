package com.motorcycleparts.motorcycleparts_master.service;

import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import com.motorcycleparts.motorcycleparts_master.repository.BrandPartsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandPartsService {
    BrandPartsRepository brandPartsRepository;

   List<BrandParts> getAllBrandParts (){
        return brandPartsRepository.findAll();
    }

}
