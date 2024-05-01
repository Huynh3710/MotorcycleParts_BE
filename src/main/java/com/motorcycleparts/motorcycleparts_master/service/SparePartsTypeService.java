package com.motorcycleparts.motorcycleparts_master.service;

import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.model.SparePartsType;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SparePartsTypeService {

    public final SparePartsTypeRepository sparePartsTypeRepository;
    public List<SparePartsType> getAllSparePartsType(){
        return sparePartsTypeRepository.findAll();
    }
}
