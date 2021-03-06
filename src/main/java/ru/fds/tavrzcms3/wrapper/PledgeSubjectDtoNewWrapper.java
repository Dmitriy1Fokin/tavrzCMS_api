package ru.fds.tavrzcms3.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class PledgeSubjectDtoNewWrapper {

    @Valid
    private PledgeSubjectDto pledgeSubjectDto;
    @Valid
    private CostHistoryDto costHistoryDto;
    @Valid
    private MonitoringDto monitoringDto;
    @NotNull
    private List<Long> pledgeAgreementsIds;

}
