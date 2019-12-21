package ru.fds.tavrzcms3.converver.dtoconverter;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubjectRoom;
import ru.fds.tavrzcms3.dto.PledgeSubjectRoomDto;

@Component
public class PledgeSubjectRoomConverter implements ConverterDto<PledgeSubjectRoom, PledgeSubjectRoomDto> {

    @Override
    public PledgeSubjectRoom toEntity(PledgeSubjectRoomDto dto) {

        return PledgeSubjectRoom.builder()
                .area(dto.getArea())
                .cadastralNum(dto.getCadastralNum())
                .conditionalNum(dto.getConditionalNum())
                .floorLocation(dto.getFloorLocation())
                .marketSegmentRoom(dto.getMarketSegmentRoom())
                .marketSegmentBuilding(dto.getMarketSegmentBuilding())
                .build();
    }

    @Override
    public PledgeSubjectRoomDto toDto(PledgeSubjectRoom entity) {

        return PledgeSubjectRoomDto.builder()
                .area(entity.getArea())
                .cadastralNum(entity.getCadastralNum())
                .conditionalNum(entity.getConditionalNum())
                .floorLocation(entity.getFloorLocation())
                .marketSegmentRoom(entity.getMarketSegmentRoom())
                .marketSegmentBuilding(entity.getMarketSegmentBuilding())
                .build();
    }
}
