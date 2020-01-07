package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class PremiseUpdate {
                    
    @Value("${excel_table.import.pledge_subject_update.ps_room.area}")
    int area;
    @Value("${excel_table.import.pledge_subject_update.ps_room.cadastral_num}")
    int cadastralNum;
    @Value("${excel_table.import.pledge_subject_update.ps_room.conditional_num}")
    int conditionalNum;
    @Value("${excel_table.import.pledge_subject_update.ps_room.floor_location}")
    int floorLocation;
    @Value("${excel_table.import.pledge_subject_update.ps_room.market_segment_room}")
    int marketSegmentRoom;
    @Value("${excel_table.import.pledge_subject_update.ps_room.market_segment_building}")
    int marketSegmentBuilding;
}
