package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.MonitoringConverterDto;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.MonitoringDto;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitoringConverterDtoTest {

    @Autowired
    MonitoringConverterDto monitoringConverter;

    @Test
    public void toEntity() {
        MonitoringDto monitoringDto = MonitoringDto.builder()
                .monitoringId(1L)
                .dateMonitoring(LocalDate.now())
                .statusMonitoring(StatusOfMonitoring.IN_STOCK)
                .employee("QWE")
                .typeOfMonitoring(TypeOfMonitoring.VISUAL)
                .notice("ASD")
                .collateralValue(1234.0)
                .pledgeSubjectId(2L)
                .build();

        Monitoring monitoring = monitoringConverter.toEntity(monitoringDto);

        assertEquals(monitoring.getMonitoringId(), monitoringDto.getMonitoringId());
        assertEquals(monitoring.getDateMonitoring(), monitoringDto.getDateMonitoring());
        assertEquals(monitoring.getStatusMonitoring(), monitoringDto.getStatusMonitoring());
        assertEquals(monitoring.getEmployee(), monitoringDto.getEmployee());
        assertEquals(monitoring.getTypeOfMonitoring(), monitoringDto.getTypeOfMonitoring());
        assertEquals(monitoring.getNotice(), monitoringDto.getNotice());
        assertEquals(monitoring.getCollateralValue(), monitoringDto.getCollateralValue());
        assertEquals(monitoring.getPledgeSubject().getPledgeSubjectId(), monitoringDto.getPledgeSubjectId());
    }

    @Test
    public void toDto() {
        Monitoring monitoring = Monitoring.builder()
                .monitoringId(1L)
                .dateMonitoring(LocalDate.now())
                .statusMonitoring(StatusOfMonitoring.IN_STOCK)
                .employee("QWE")
                .typeOfMonitoring(TypeOfMonitoring.VISUAL)
                .notice("ASD")
                .collateralValue(1234.0)
                .pledgeSubject(new PledgeSubject().builder().pledgeSubjectId(2L).build())
                .build();

        MonitoringDto monitoringDto = monitoringConverter.toDto(monitoring);

        assertEquals(monitoring.getMonitoringId(), monitoringDto.getMonitoringId());
        assertEquals(monitoring.getDateMonitoring(), monitoringDto.getDateMonitoring());
        assertEquals(monitoring.getStatusMonitoring(), monitoringDto.getStatusMonitoring());
        assertEquals(monitoring.getEmployee(), monitoringDto.getEmployee());
        assertEquals(monitoring.getTypeOfMonitoring(), monitoringDto.getTypeOfMonitoring());
        assertEquals(monitoring.getNotice(), monitoringDto.getNotice());
        assertEquals(monitoring.getCollateralValue(), monitoringDto.getCollateralValue());
        assertEquals(monitoring.getPledgeSubject().getPledgeSubjectId(), monitoringDto.getPledgeSubjectId());
    }
}