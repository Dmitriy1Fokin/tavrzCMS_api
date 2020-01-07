package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class SecuritiesUpdate {
                    
    @Value("${excel_table.import.pledge_subject_update.ps_securities.nominal_value}")
    int nominalValue;
    @Value("${excel_table.import.pledge_subject_update.ps_securities.actual_value}")
    int actualValue;
    @Value("${excel_table.import.pledge_subject_update.ps_securities.type_of_securities}")
    int typeOfSecurities;
}
