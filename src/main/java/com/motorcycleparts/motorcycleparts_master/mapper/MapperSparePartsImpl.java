package com.motorcycleparts.motorcycleparts_master.mapper;

import com.motorcycleparts.motorcycleparts_master.Dto.SparePartsDto;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperSparePartsImpl implements MapperDto<SpareParts, SparePartsDto>{

    private final ModelMapper modelMapper;

    @Override
    public SparePartsDto mapTo(SpareParts spareParts) {
        return  modelMapper.map(spareParts, SparePartsDto.class);
    }

    @Override
    public SpareParts mapFrom(SparePartsDto sparePartsDto) {
        return  modelMapper.map(sparePartsDto, SpareParts.class);
    }
}
