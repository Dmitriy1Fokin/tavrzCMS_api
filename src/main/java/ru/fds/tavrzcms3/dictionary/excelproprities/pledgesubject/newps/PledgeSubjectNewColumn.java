package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.newps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class PledgeSubjectNewColumn {
    AutoNew auto;
    EquipmentNew equipment;
    BuildingNew building;
    LandLeaseNew landLease;
    LandOwnershipNew landOwnership;
    PremiseNew premise;
    SecuritiesNew securities;
    TboNew tbo;
    VesselNew vessel;

    public PledgeSubjectNewColumn(AutoNew auto,
                                  EquipmentNew equipment,
                                  BuildingNew building,
                                  LandLeaseNew landLease,
                                  LandOwnershipNew landOwnership,
                                  PremiseNew premise,
                                  SecuritiesNew securities,
                                  TboNew tbo,
                                  VesselNew vessel){
        this.auto = auto;
        this.equipment = equipment;
        this.building = building;
        this.landLease = landLease;
        this.landOwnership = landOwnership;
        this.premise = premise;
        this.securities = securities;
        this.tbo = tbo;
        this.vessel = vessel;
    }

    @Value("${excel_table.import.pledge_subject_new.name}")
    int name;
    @Value("${excel_table.import.pledge_subject_new.liquidity}")
    int liquidity;
    @Value("${excel_table.import.pledge_subject_new.type_of_pledge}")
    int typeOfPledge;
    @Value("${excel_table.import.pledge_subject_new.address_region}")
    int addressRegion;
    @Value("${excel_table.import.pledge_subject_new.address_district}")
    int addressDistrict;
    @Value("${excel_table.import.pledge_subject_new.address_city}")
    int addressCity;
    @Value("${excel_table.import.pledge_subject_new.address_street}")
    int addressStreet;
    @Value("${excel_table.import.pledge_subject_new.address_building}")
    int addressBuilding;
    @Value("${excel_table.import.pledge_subject_new.address_premises}")
    int addressPremises;
    @Value("${excel_table.import.pledge_subject_new.insurance_obligation}")
    int insuranceObligation;
    @Value("${excel_table.import.pledge_subject_new.pledge_agreements_Id}")
    int pledgeAgreementsIds;

    @Value("${excel_table.import.pledge_subject_new.date_conclusion}")
    int dateConclusion;
    @Value("${excel_table.import.pledge_subject_new.zs_dz}")
    int zsDz;
    @Value("${excel_table.import.pledge_subject_new.zs_zz}")
    int zsZZ;
    @Value("${excel_table.import.pledge_subject_new.rs_dz}")
    int rsDz;
    @Value("${excel_table.import.pledge_subject_new.rs_zz}")
    int rsZZ;
    @Value("${excel_table.import.pledge_subject_new.ss}")
    int ss;
    @Value("${excel_table.import.pledge_subject_new.appraiser}")
    int appraiser;
    @Value("${excel_table.import.pledge_subject_new.num_appraisal_report}")
    int numAppraisalReport;
    @Value("${excel_table.import.pledge_subject_new.date_appraisal_report}")
    int dateAppraisalReport;

    @Value("${excel_table.import.pledge_subject_new.date_monitoring}")
    int dateMonitoring;
    @Value("${excel_table.import.pledge_subject_new.status_monitoring}")
    int statusMonitoring;
    @Value("${excel_table.import.pledge_subject_new.employee}")
    int employee;
    @Value("${excel_table.import.pledge_subject_new.type_monitoring}")
    int typeOfMonitoring;
    @Value("${excel_table.import.pledge_subject_new.collateral_value}")
    int collateralValue;
    @Value("${excel_table.import.pledge_subject_new.notice}")
    int notice;
}
