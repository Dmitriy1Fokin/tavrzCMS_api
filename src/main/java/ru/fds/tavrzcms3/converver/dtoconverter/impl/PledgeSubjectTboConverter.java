package ru.fds.tavrzcms3.converver.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converver.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectTBO;
import ru.fds.tavrzcms3.dto.PledgeSubjectTboDto;

@Component
public class PledgeSubjectTboConverter implements ConverterDto<PledgeSubjectTBO, PledgeSubjectTboDto> {

    @Override
    public PledgeSubjectTBO toEntity(PledgeSubjectTboDto dto) {

        return PledgeSubjectTBO.builder()
                .countOfTBO(dto.getCountOfTBO())
                .carryingAmount(dto.getCarryingAmount())
                .typeOfTBO(dto.getTypeOfTBO())
                .build();
    }

    @Override
    public PledgeSubjectTboDto toDto(PledgeSubjectTBO entity) {

        return PledgeSubjectTboDto.builder()
                .countOfTBO(entity.getCountOfTBO())
                .carryingAmount(entity.getCarryingAmount())
                .typeOfTBO(entity.getTypeOfTBO())
                .build();
    }
}
