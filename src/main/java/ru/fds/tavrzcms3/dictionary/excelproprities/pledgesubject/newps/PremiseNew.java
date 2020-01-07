package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.newps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class PremiseNew {
                    
    @Value("${excel_table.import.pledge_subject_new.ps_room.area}")
    int area;
    @Value("${excel_table.import.pledge_subject_new.ps_room.cadastral_num}")
    int cadastralNum;
    @Value("${excel_table.import.pledge_subject_new.ps_room.conditional_num}")
    int conditionalNum;
    @Value("${excel_table.import.pledge_subject_new.ps_room.floor_location}")
    int floorLocation;
    @Value("${excel_table.import.pledge_subject_new.ps_room.market_segment_room}")
    int marketSegmentRoom;
    @Value("${excel_table.import.pledge_subject_new.ps_room.market_segment_building}")
    int marketSegmentBuilding;
}
