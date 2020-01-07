package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.newps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class BuildingNew {

    @Value("${excel_table.import.pledge_subject_new.ps_building.area}")
    int area;
    @Value("${excel_table.import.pledge_subject_new.ps_building.cadastral_num}")
    int cadastralNum;
    @Value("${excel_table.import.pledge_subject_new.ps_building.conditional_num}")
    int conditionalNum;
    @Value("${excel_table.import.pledge_subject_new.ps_building.readiness_degree}")
    int readinessDegree;
    @Value("${excel_table.import.pledge_subject_new.ps_building.year_of_construction}")
    int yearOfConstruction;
    @Value("${excel_table.import.pledge_subject_new.ps_building.market_segment}")
    int marketSegment;
}
