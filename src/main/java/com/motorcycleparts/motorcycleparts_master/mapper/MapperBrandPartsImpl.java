package com.motorcycleparts.motorcycleparts_master.mapper;

import com.motorcycleparts.motorcycleparts_master.Dto.BrandPartsDto;
import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MapperBrandPartsImpl implements MapperDto<BrandParts, BrandPartsDto>{

    private ModelMapper modelMapper;
    @Override
    public BrandPartsDto mapTo(BrandParts brandParts) {
        return modelMapper.map(brandParts, BrandPartsDto.class);
    }

    @Override
    public BrandParts mapFrom(BrandPartsDto brandPartsDto) {
        return modelMapper.map(brandPartsDto, BrandParts.class);
    }
}
