package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.dto.PledgeSubjectSecuritiesDto;

@Component
public class PledgeSubjectSecuritiesConverter implements ConverterDto<PledgeSubjectSecurities, PledgeSubjectSecuritiesDto> {

    @Override
    public PledgeSubjectSecurities toEntity(PledgeSubjectSecuritiesDto dto) {

        return PledgeSubjectSecurities.builder()
                .nominalValue(dto.getNominalValue())
                .actualValue(dto.getActualValue())
                .typeOfSecurities(dto.getTypeOfSecurities())
                .build();
    }

    @Override
    public PledgeSubjectSecuritiesDto toDto(PledgeSubjectSecurities entity) {

        return PledgeSubjectSecuritiesDto.builder()
                .nominalValue(entity.getNominalValue())
                .actualValue(entity.getActualValue())
                .typeOfSecurities(entity.getTypeOfSecurities())
                .build();
    }
}
