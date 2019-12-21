package ru.fds.tavrzcms3.converver.dtoconverter;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubjectBuilding;
import ru.fds.tavrzcms3.dto.PledgeSubjectBuildingDto;

@Component
public class PledgeSubjectBuildingConverter implements ConverterDto<PledgeSubjectBuilding, PledgeSubjectBuildingDto> {

    @Override
    public PledgeSubjectBuilding toEntity(PledgeSubjectBuildingDto dto) {

        return PledgeSubjectBuilding.builder()
                .area(dto.getArea())
                .cadastralNum(dto.getCadastralNum())
                .conditionalNum(dto.getConditionalNum())
                .readinessDegree(dto.getReadinessDegree())
                .yearOfConstruction(dto.getYearOfConstruction())
                .marketSegment(dto.getMarketSegment())
                .build();
    }

    @Override
    public PledgeSubjectBuildingDto toDto(PledgeSubjectBuilding entity) {

        return PledgeSubjectBuildingDto.builder()
                .area(entity.getArea())
                .cadastralNum(entity.getCadastralNum())
                .conditionalNum(entity.getConditionalNum())
                .readinessDegree(entity.getReadinessDegree())
                .yearOfConstruction(entity.getYearOfConstruction())
                .marketSegment(entity.getMarketSegment())
                .build();
    }
}
