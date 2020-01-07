package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.newps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class TboNew {
                    
    @Value("${excel_table.import.pledge_subject_new.ps_tbo.count_of_tbo}")
    int count;
    @Value("${excel_table.import.pledge_subject_new.ps_tbo.carrying_amount}")
    int carryingAmount;
    @Value("${excel_table.import.pledge_subject_new.ps_tbo.type_of_tbo}")
    int typeOfTbo;
}
