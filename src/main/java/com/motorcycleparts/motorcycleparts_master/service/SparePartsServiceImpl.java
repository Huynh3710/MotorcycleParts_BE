package com.motorcycleparts.motorcycleparts_master.service;

import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.exception.NotFoundException;
import com.motorcycleparts.motorcycleparts_master.mapper.MapperSparePartsImpl;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SparePartsServiceImpl implements SparePartsService{


    private final SparePartsRepository sparePartsRepository;
    private final MapperSparePartsImpl mapperSpareParts;
    @Override
    public List<SparePartsDto> getAllParts() {
        List<SpareParts> spareParts = sparePartsRepository.findAll();
        return spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList());
    }

    @Override
    public SparePartsDto getSparePartsById(Long id) {
        SpareParts spareParts =
                sparePartsRepository.findById(id)
                        .orElseThrow(()->new NotFoundException(String.format("SpareParts with "+id+"not found!")));
        return mapperSpareParts.mapTo(spareParts);
    }

    @Override
    public List<SparePartsDto> getSparePartsByBrandId(Long id) {
        List<SpareParts> spareParts = sparePartsRepository.findSparePartsByBrandPartsId(id);
        return spareParts.stream().map(mapperSpareParts::mapTo).collect(Collectors.toList());
    }
}
