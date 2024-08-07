package com.motorcycleparts.motorcycleparts_master.service;

import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SparePartsService {
    List<SparePartsDto> getAllParts();
    SparePartsDto getSparePartsById(Long id);
    List<SparePartsDto> getSparePartsByBrandId(Long id);

    List<SparePartsDto> getSparePartsByTypeId(Long id);

    Page<SparePartsDto> getAllPartsPages(Pageable page);
}
