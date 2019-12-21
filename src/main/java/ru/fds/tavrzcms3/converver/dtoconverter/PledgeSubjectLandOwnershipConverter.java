package ru.fds.tavrzcms3.converver.dtoconverter;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubjectLandOwnership;
import ru.fds.tavrzcms3.dto.PledgeSubjectLandOwnershipDto;

@Component
public class PledgeSubjectLandOwnershipConverter implements ConverterDto<PledgeSubjectLandOwnership, PledgeSubjectLandOwnershipDto> {

    @Override
    public PledgeSubjectLandOwnership toEntity(PledgeSubjectLandOwnershipDto dto) {

        return PledgeSubjectLandOwnership.builder()
                .area(dto.getArea())
                .cadastralNum(dto.getCadastralNum())
                .conditionalNum(dto.getConditionalNum())
                .permittedUse(dto.getPermittedUse())
                .builtUp(dto.getBuiltUp())
                .cadastralNumOfBuilding(dto.getCadastralNumOfBuilding())
                .landCategory(dto.getLandCategory())
                .build();
    }

    @Override
    public PledgeSubjectLandOwnershipDto toDto(PledgeSubjectLandOwnership entity) {

        return PledgeSubjectLandOwnershipDto.builder()
                .area(entity.getArea())
                .cadastralNum(entity.getCadastralNum())
                .conditionalNum(entity.getConditionalNum())
                .permittedUse(entity.getPermittedUse())
                .builtUp(entity.getBuiltUp())
                .cadastralNumOfBuilding(entity.getCadastralNumOfBuilding())
                .landCategory(entity.getLandCategory())
                .build();
    }
}
