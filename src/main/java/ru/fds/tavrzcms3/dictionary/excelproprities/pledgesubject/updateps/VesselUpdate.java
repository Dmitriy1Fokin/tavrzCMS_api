package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class VesselUpdate {
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.imo}")
    int imo;
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.mmsi}")
    int mmsi;
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.flag}")
    int flag;
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.vessel_type}")
    int vesselType;
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.gross_tonnage}")
    int grossTonnage;
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.deadweight}")
    int deadweight;
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.year_built}")
    int yearOfBuilt;
    @Value("${excel_table.import.pledge_subject_update.ps_vessel.status}")
    int status;
}
