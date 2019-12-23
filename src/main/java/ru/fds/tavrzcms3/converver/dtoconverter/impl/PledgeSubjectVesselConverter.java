package ru.fds.tavrzcms3.converver.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converver.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectVessel;
import ru.fds.tavrzcms3.dto.PledgeSubjectVesselDto;

@Component
public class PledgeSubjectVesselConverter implements ConverterDto<PledgeSubjectVessel, PledgeSubjectVesselDto> {

    @Override
    public PledgeSubjectVessel toEntity(PledgeSubjectVesselDto dto) {

        return PledgeSubjectVessel.builder()
                .imo(dto.getImo())
                .mmsi(dto.getMmsi())
                .flag(dto.getFlag())
                .vesselType(dto.getVesselType())
                .grossTonnage(dto.getGrossTonnage())
                .deadweight(dto.getDeadweight())
                .yearBuilt(dto.getYearBuilt())
                .statusVessel(dto.getStatusVessel())
                .build();
    }

    @Override
    public PledgeSubjectVesselDto toDto(PledgeSubjectVessel entity) {

        return PledgeSubjectVesselDto.builder()
                .imo(entity.getImo())
                .mmsi(entity.getMmsi())
                .flag(entity.getFlag())
                .vesselType(entity.getVesselType())
                .grossTonnage(entity.getGrossTonnage())
                .deadweight(entity.getDeadweight())
                .yearBuilt(entity.getYearBuilt())
                .statusVessel(entity.getStatusVessel())
                .build();
    }
}
