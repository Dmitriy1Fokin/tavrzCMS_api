package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

@Component
public class MonitoringConverterDto implements ConverterDto<Monitoring, MonitoringDto> {

    private final PledgeSubjectService pledgeSubjectService;

    public MonitoringConverterDto(PledgeSubjectService pledgeSubjectService) {
        this.pledgeSubjectService = pledgeSubjectService;
    }

    @Override
    public Monitoring toEntity(MonitoringDto dto) {
        return Monitoring.builder()
                .monitoringId(dto.getMonitoringId())
                .dateMonitoring(dto.getDateMonitoring())
                .statusMonitoring(dto.getStatusMonitoring())
                .employee(dto.getEmployee())
                .typeOfMonitoring(dto.getTypeOfMonitoring())
                .notice(dto.getNotice())
                .collateralValue(dto.getCollateralValue())
                .pledgeSubject(pledgeSubjectService.getPledgeSubjectById(dto.getPledgeSubjectId()).orElse(null))
                .build();
    }

    @Override
    public MonitoringDto toDto(Monitoring entity) {
        return MonitoringDto.builder()
                .monitoringId(entity.getMonitoringId())
                .dateMonitoring(entity.getDateMonitoring())
                .statusMonitoring(entity.getStatusMonitoring())
                .employee(entity.getEmployee())
                .typeOfMonitoring(entity.getTypeOfMonitoring())
                .notice(entity.getNotice())
                .collateralValue(entity.getCollateralValue())
                .pledgeSubjectId(entity.getPledgeSubject().getPledgeSubjectId())
                .build();
    }
}
