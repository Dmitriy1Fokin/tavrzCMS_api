package ru.fds.tavrzcms3.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class PledgeSubjectUpdateDtoWrapper {

    @Valid
    private PledgeSubjectDto pledgeSubjectDto;

    @NotNull
    private List<Long> pledgeAgreementsIds;
}
