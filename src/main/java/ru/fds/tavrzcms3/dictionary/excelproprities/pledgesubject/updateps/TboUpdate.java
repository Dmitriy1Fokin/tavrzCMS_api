package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class TboUpdate {
    @Value("${excel_table.import.pledge_subject_update.ps_tbo.count_of_tbo}")
    int count;
    @Value("${excel_table.import.pledge_subject_update.ps_tbo.carrying_amount}")
    int carryingAmount;
    @Value("${excel_table.import.pledge_subject_update.ps_tbo.type_of_tbo}")
    int typeOfTbo;
}
