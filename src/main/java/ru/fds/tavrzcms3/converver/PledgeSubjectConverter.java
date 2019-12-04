package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;

@Component
public class PledgeSubjectConverter implements Converter<PledgeSubject, PledgeSubjectDto> {
    @Override
    public PledgeSubject toEntity(PledgeSubjectDto dto) {
        return null;
    }

    @Override
    public PledgeSubjectDto toDto(PledgeSubject entity) {
        return null;
    }
}
