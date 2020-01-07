package ru.fds.tavrzcms3.dictionary.excelproprities.monitoring;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class MonitoringUpdateColumn {

    @Value("${excel_table.import.monitoring_update.monitoring_id}")
    int monitoringId;
    @Value("${excel_table.import.monitoring_update.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.monitoring_update.date}")
    int date;
    @Value("${excel_table.import.monitoring_update.status}")
    int status;
    @Value("${excel_table.import.monitoring_update.employee}")
    int employee;
    @Value("${excel_table.import.monitoring_update.type}")
    int typeOfMonitoring;
    @Value("${excel_table.import.monitoring_update.collateral_value}")
    int collateralValue;
    @Value("${excel_table.import.monitoring_update.notice}")
    int notice;
}
