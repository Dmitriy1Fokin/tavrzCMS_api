package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.dto.EncumbranceDto;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

@Component
public class EncumbranceConverter implements Converter<Encumbrance, EncumbranceDto> {

    private final PledgeSubjectService pledgeSubjectService;

    public EncumbranceConverter(PledgeSubjectService pledgeSubjectService) {
        this.pledgeSubjectService = pledgeSubjectService;
    }


    @Override
    public Encumbrance toEntity(EncumbranceDto dto) {
        return Encumbrance.builder()
                .encumbranceId(dto.getEncumbranceId())
                .nameEncumbrance(dto.getNameEncumbrance())
                .typeOfEncumbrance(dto.getTypeOfEncumbrance())
                .inFavorOfWhom(dto.getInFavorOfWhom())
                .dateBegin(dto.getDateBegin())
                .dateEnd(dto.getDateEnd())
                .numOfEncumbrance(dto.getNumOfEncumbrance())
                .pledgeSubject(pledgeSubjectService.getPledgeSubjectById(dto.getPledgeSubjectId()).orElse(null))
                .build();
    }

    @Override
    public EncumbranceDto toDto(Encumbrance entity) {
        return EncumbranceDto.builder()
                .encumbranceId(entity.getEncumbranceId())
                .nameEncumbrance(entity.getNameEncumbrance())
                .typeOfEncumbrance(entity.getTypeOfEncumbrance())
                .inFavorOfWhom(entity.getInFavorOfWhom())
                .dateBegin(entity.getDateBegin())
                .dateEnd(entity.getDateEnd())
                .numOfEncumbrance(entity.getNumOfEncumbrance())
                .pledgeSubjectId(entity.getPledgeSubject().getPledgeSubjectId())
                .build();
    }
}
