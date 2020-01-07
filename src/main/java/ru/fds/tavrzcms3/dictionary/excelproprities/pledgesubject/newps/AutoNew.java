package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.newps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class AutoNew {
    @Value("${excel_table.import.pledge_subject_new.ps_auto.brand}")
    int brand;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.model}")
    int model;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.vin}")
    int vin;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.num_of_engine}")
    int numOfEngine;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.num_of_pts}")
    int numOfPts;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.year_of_manufacture}")
    int yearOfManufacture;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.inventory_num}")
    int inventoryNum;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.type_of_auto}")
    int typeOfAuto;
    @Value("${excel_table.import.pledge_subject_new.ps_auto.horsepower}")
    int horsepower;
}
