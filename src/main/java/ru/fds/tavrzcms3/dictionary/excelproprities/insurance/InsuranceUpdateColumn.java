package ru.fds.tavrzcms3.dictionary.excelproprities.insurance;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class InsuranceUpdateColumn {

    @Value("${excel_table.import.insurance_update.insurance_id}")
    int insuranceId;
    @Value("${excel_table.import.insurance_update.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.insurance_update.num_insurance}")
    int numInsurance;
    @Value("${excel_table.import.insurance_update.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.insurance_update.date_end}")
    int dateEnd;
    @Value("${excel_table.import.insurance_update.sum_insured}")
    int sumInsured;
    @Value("${excel_table.import.insurance_update.date_insurance_contract}")
    int dateInsuranceContract;
    @Value("${excel_table.import.insurance_update.payment_of_insurance_premium}")
    int paymentOfInsurancePremium;
    @Value("${excel_table.import.insurance_update.franchise_amount}")
    int franchiseAmount;
}
