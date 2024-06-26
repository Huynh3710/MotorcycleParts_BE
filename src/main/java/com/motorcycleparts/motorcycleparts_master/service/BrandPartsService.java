package com.motorcycleparts.motorcycleparts_master.service;

import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import com.motorcycleparts.motorcycleparts_master.repository.BrandPartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandPartsService {
    private final BrandPartsRepository brandPartsRepository;
    BrandPartsService brandPartsService;

   public List<BrandParts> getAllBrandParts (){

       return brandPartsRepository.findAll();
    }


}
