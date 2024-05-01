package com.motorcycleparts.motorcycleparts_master.mapper;

import com.motorcycleparts.motorcycleparts_master.Dto.MotorTypeDto;
import com.motorcycleparts.motorcycleparts_master.model.MotorType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MapperMotorTypesImpl implements MapperDto<MotorType, MotorTypeDto>{
    private final ModelMapper modelMapper;
    @Override
    public MotorTypeDto mapTo(MotorType motorType) {
        return modelMapper.map(motorType, MotorTypeDto.class);
    }

    @Override
    public MotorType mapFrom(MotorTypeDto motorTypeDto) {
        return modelMapper.map(motorTypeDto, MotorType.class);
    }
}
