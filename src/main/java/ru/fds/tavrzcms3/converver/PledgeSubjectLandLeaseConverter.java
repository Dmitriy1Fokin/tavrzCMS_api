package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubjectLandLease;
import ru.fds.tavrzcms3.dto.PledgeSubjectLandLeaseDto;

@Component
public class PledgeSubjectLandLeaseConverter implements ConverterDto<PledgeSubjectLandLease, PledgeSubjectLandLeaseDto> {

    @Override
    public PledgeSubjectLandLease toEntity(PledgeSubjectLandLeaseDto dto) {

        return PledgeSubjectLandLease.builder()
                .area(dto.getArea())
                .cadastralNum(dto.getCadastralNum())
                .conditionalNum(dto.getConditionalNum())
                .permittedUse(dto.getPermittedUse())
                .builtUp(dto.getBuiltUp())
                .cadastralNumOfBuilding(dto.getCadastralNumOfBuilding())
                .dateBeginLease(dto.getDateBeginLease())
                .dateEndLease(dto.getDateEndLease())
                .landCategory(dto.getLandCategory())
                .build();
    }

    @Override
    public PledgeSubjectLandLeaseDto toDto(PledgeSubjectLandLease entity) {

        return PledgeSubjectLandLeaseDto.builder()
                .area(entity.getArea())
                .cadastralNum(entity.getCadastralNum())
                .conditionalNum(entity.getConditionalNum())
                .permittedUse(entity.getPermittedUse())
                .builtUp(entity.getBuiltUp())
                .cadastralNumOfBuilding(entity.getCadastralNumOfBuilding())
                .dateBeginLease(entity.getDateBeginLease())
                .dateEndLease(entity.getDateEndLease())
                .landCategory(entity.getLandCategory())
                .build();
    }
}
