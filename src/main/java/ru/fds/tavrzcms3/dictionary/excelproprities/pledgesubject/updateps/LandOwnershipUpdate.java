package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class LandOwnershipUpdate {
                    
    @Value("${excel_table.import.pledge_subject_update.ps_land_owner.area}")
    int area;
    @Value("${excel_table.import.pledge_subject_update.ps_land_owner.cadastral_num}")
    int cadastralNum;
    @Value("${excel_table.import.pledge_subject_update.ps_land_owner.conditional_num}")
    int conditionalNum;
    @Value("${excel_table.import.pledge_subject_update.ps_land_owner.permitted_use}")
    int permittedUse;
    @Value("${excel_table.import.pledge_subject_update.ps_land_owner.built_up}")
    int builtUp;
    @Value("${excel_table.import.pledge_subject_update.ps_land_owner.cadastral_num_of_building}")
    int cadastralNumOfBuilding;
    @Value("${excel_table.import.pledge_subject_update.ps_land_owner.land_category}")
    int landCategory;
}
