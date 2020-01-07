package ru.fds.tavrzcms3.dictionary.excelproprities.monitoring;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class MonitoringNewColumn {

    @Value("${excel_table.import.monitoring_new.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.monitoring_new.date}")
    int date;
    @Value("${excel_table.import.monitoring_new.status}")
    int status;
    @Value("${excel_table.import.monitoring_new.employee}")
    int employee;
    @Value("${excel_table.import.monitoring_new.type}")
    int typeOfMonitoring;
    @Value("${excel_table.import.monitoring_new.collateral_value}")
    int collateralValue;
    @Value("${excel_table.import.monitoring_new.notice}")
    int notice;
}
