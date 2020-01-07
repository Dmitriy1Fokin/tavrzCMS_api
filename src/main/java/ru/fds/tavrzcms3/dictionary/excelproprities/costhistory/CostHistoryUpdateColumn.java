package ru.fds.tavrzcms3.dictionary.excelproprities.costhistory;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class CostHistoryUpdateColumn {

    @Value("${excel_table.import.cost_history_update.cost_history_id}")
    int costHistoryId;
    @Value("${excel_table.import.cost_history_update.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.cost_history_update.date}")
    int date;
    @Value("${excel_table.import.cost_history_update.zs_dz}")
    int zsDz;
    @Value("${excel_table.import.cost_history_update.zs_zz}")
    int zsZz;
    @Value("${excel_table.import.cost_history_update.rs_dz}")
    int rsDz;
    @Value("${excel_table.import.cost_history_update.rs_zz}")
    int rsZZ;
    @Value("${excel_table.import.cost_history_update.ss}")
    int ss;
    @Value("${excel_table.import.cost_history_update.appraiser}")
    int appraiser;
    @Value("${excel_table.import.cost_history_update.num_appraisal_report}")
    int numAppraisalReport;
    @Value("${excel_table.import.cost_history_update.date_appraisal_report}")
    int dateAppraisalReport;
}
