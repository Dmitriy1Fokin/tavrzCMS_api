package ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class EquipmentUpdate {
                    
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.brand}")
    int brand;
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.model}")
    int model;
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.serial_number}")
    int serialNumber;
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.year_of_manufacture}")
    int yearOfManufacture;
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.inventory_number}")
    int inventoryNum;
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.type_of_equipment}")
    int typeOfEquipment;
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.productivity}")
    int productivity;
    @Value("${excel_table.import.pledge_subject_update.ps_equipment.type_of_productivity}")
    int typeOfProductivity;
}
