package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectAuto;
import ru.fds.tavrzcms3.dto.PledgeSubjectAutoDto;

@Component
public class PledgeSubjectAutoConverter implements ConverterDto<PledgeSubjectAuto, PledgeSubjectAutoDto> {

    @Override
    public PledgeSubjectAuto toEntity(PledgeSubjectAutoDto dto) {

        return PledgeSubjectAuto.builder()
                .brandAuto(dto.getBrand())
                .modelAuto(dto.getModel())
                .vin(dto.getVin())
                .numOfEngine(dto.getNumOfEngine())
                .numOfPTS(dto.getNumOfPTS())
                .yearOfManufactureAuto(dto.getYearOfManufacture())
                .inventoryNumAuto(dto.getInventoryNum())
                .typeOfAuto(dto.getTypeOfAuto())
                .horsepower(dto.getHorsepower())
                .build();
    }

    @Override
    public PledgeSubjectAutoDto toDto(PledgeSubjectAuto entity) {

        return PledgeSubjectAutoDto.builder()
                .brand(entity.getBrandAuto())
                .model(entity.getModelAuto())
                .vin(entity.getVin())
                .numOfEngine(entity.getNumOfEngine())
                .numOfPTS(entity.getNumOfPTS())
                .yearOfManufacture(entity.getYearOfManufactureAuto())
                .inventoryNum(entity.getInventoryNumAuto())
                .typeOfAuto(entity.getTypeOfAuto())
                .horsepower(entity.getHorsepower())
                .build();
    }
}
