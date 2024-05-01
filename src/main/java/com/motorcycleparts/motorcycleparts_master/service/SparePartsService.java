package com.motorcycleparts.motorcycleparts_master.service;

import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SparePartsService {
    List<SparePartsDto> getAllParts();
    SparePartsDto getSparePartsById(Long id);
    List<SparePartsDto> getSparePartsByBrandId(Long id);

    List<SparePartsDto> getSparePartsByTypeId(Long id);
}
