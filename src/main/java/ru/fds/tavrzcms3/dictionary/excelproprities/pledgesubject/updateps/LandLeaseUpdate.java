package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class LandLeaseUpdate {
                    
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.area}")
    int area;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.cadastral_num}")
    int cadastralNum;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.conditional_num}")
    int conditionalNum;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.permitted_use}")
    int permittedUse;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.built_up}")
    int builtUp;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.cadastral_num_of_building}")
    int cadastralNumOfBuilding;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.begin_date}")
    int dateBegin;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.end_date}")
    int dateEnd;
    @Value("${excel_table.import.pledge_subject_update.ps_land_lease.land_category}")
    int landCategory;
}
