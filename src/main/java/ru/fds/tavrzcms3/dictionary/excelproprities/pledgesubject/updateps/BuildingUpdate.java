package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class BuildingUpdate {
                    
    @Value("${excel_table.import.pledge_subject_update.ps_building.area}")
    int area;
    @Value("${excel_table.import.pledge_subject_update.ps_building.cadastral_num}")
    int cadastralNum;
    @Value("${excel_table.import.pledge_subject_update.ps_building.conditional_num}")
    int conditionalNum;
    @Value("${excel_table.import.pledge_subject_update.ps_building.readiness_degree}")
    int readinessDegree;
    @Value("${excel_table.import.pledge_subject_update.ps_building.year_of_construction}")
    int yearOfConstruction;
    @Value("${excel_table.import.pledge_subject_update.ps_building.market_segment}")
    int marketSegment;
}
