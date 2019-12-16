package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.dto.PledgeSubjectEquipmentDto;

@Component
public class PledgeSubjectEquipmentConverter implements ConverterDto<PledgeSubjectEquipment, PledgeSubjectEquipmentDto> {

    @Override
    public PledgeSubjectEquipment toEntity(PledgeSubjectEquipmentDto dto) {

        return PledgeSubjectEquipment.builder()
                .brandEquip(dto.getBrand())
                .modelEquip(dto.getModel())
                .serialNum(dto.getSerialNum())
                .yearOfManufactureEquip(dto.getYearOfManufacture())
                .inventoryNumEquip(dto.getInventoryNum())
                .typeOfEquipment(dto.getTypeOfEquipment())
                .productivity(dto.getProductivity())
                .typeOfProductivity(dto.getTypeOfProductivity())
                .build();
    }

    @Override
    public PledgeSubjectEquipmentDto toDto(PledgeSubjectEquipment entity) {

        return PledgeSubjectEquipmentDto.builder()
                .brand(entity.getBrandEquip())
                .model(entity.getModelEquip())
                .serialNum(entity.getSerialNum())
                .yearOfManufacture(entity.getYearOfManufactureEquip())
                .inventoryNum(entity.getInventoryNumEquip())
                .typeOfEquipment(entity.getTypeOfEquipment())
                .productivity(entity.getProductivity())
                .typeOfProductivity(entity.getTypeOfProductivity())
                .build();
    }
}
