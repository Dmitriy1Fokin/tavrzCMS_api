package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.newps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class SecuritiesNew {

    @Value("${excel_table.import.pledge_subject_new.ps_securities.nominal_value}")
    int nominalValue;
    @Value("${excel_table.import.pledge_subject_new.ps_securities.actual_value}")
    int actualValue;
    @Value("${excel_table.import.pledge_subject_new.ps_securities.type_of_securities}")
    int typeOfSecurities;
}
