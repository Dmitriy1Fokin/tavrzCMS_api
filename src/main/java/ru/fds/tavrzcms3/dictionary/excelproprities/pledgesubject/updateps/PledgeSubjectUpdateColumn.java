package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class PledgeSubjectUpdateColumn {
    AutoUpdate auto;
    EquipmentUpdate equipment;
    BuildingUpdate building;
    LandLeaseUpdate landLease;
    LandOwnershipUpdate landOwnership;
    PremiseUpdate premise;
    SecuritiesUpdate securities;
    TboUpdate tbo;
    VesselUpdate vessel;

    public PledgeSubjectUpdateColumn(AutoUpdate auto,
                                     EquipmentUpdate equipment,
                                     BuildingUpdate building,
                                     LandLeaseUpdate landLease,
                                     LandOwnershipUpdate landOwnership,
                                     PremiseUpdate premise,
                                     SecuritiesUpdate securities,
                                     TboUpdate tbo,
                                     VesselUpdate vessel){
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
    @Value("${excel_table.import.pledge_subject_update.pledge_agreement_id}")
    int pledgeAgreementsIds;
    @Value("${excel_table.import.pledge_subject_update.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.pledge_subject_update.name}")
    int name;
    @Value("${excel_table.import.pledge_subject_update.liquidity}")
    int liquidity;
    @Value("${excel_table.import.pledge_subject_update.type_of_pledge}")
    int typeOfPledge;
    @Value("${excel_table.import.pledge_subject_update.address_region}")
    int addressRegion;
    @Value("${excel_table.import.pledge_subject_update.address_district}")
    int addressDistrict;
    @Value("${excel_table.import.pledge_subject_update.address_city}")
    int addressCity;
    @Value("${excel_table.import.pledge_subject_update.address_street}")
    int addressStreet;
    @Value("${excel_table.import.pledge_subject_update.address_building}")
    int addressBuilding;
    @Value("${excel_table.import.pledge_subject_update.address_premises}")
    int addressPremises;
    @Value("${excel_table.import.pledge_subject_update.insurance_obligation}")
    int insuranceObligation;
}
