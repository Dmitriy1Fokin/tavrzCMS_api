package ru.fds.tavrzcms3.dictionary.excelproprities.costhistory;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class CostHistoryNewColumn {

    @Value("${excel_table.import.cost_history_new.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.cost_history_new.date}")
    int date;
    @Value("${excel_table.import.cost_history_new.zs_dz}")
    int zsDz;
    @Value("${excel_table.import.cost_history_new.zs_zz}")
    int zsZz;
    @Value("${excel_table.import.cost_history_new.rs_dz}")
    int rsDz;
    @Value("${excel_table.import.cost_history_new.rs_zz}")
    int rsZZ;
    @Value("${excel_table.import.cost_history_new.ss}")
    int ss;
    @Value("${excel_table.import.cost_history_new.appraiser}")
    int appraiser;
    @Value("${excel_table.import.cost_history_new.num_appraisal_report}")
    int numAppraisalReport;
    @Value("${excel_table.import.cost_history_new.date_appraisal_report}")
    int dateAppraisalReport;
}
