package ru.fds.tavrzcms3.mapper;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;

@Component
public class PledgeSubjectMapper implements Mapper<PledgeSubject, PledgeSubjectDto> {
    @Override
    public PledgeSubject toEntity(PledgeSubjectDto dto) {
        return null;
    }

    @Override
    public PledgeSubjectDto toDto(PledgeSubject entity) {
        return null;
    }
}
