package ru.fds.tavrzcms3.dictionary.excelproprities.insurance;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class InsuranceNewColumn {

    @Value("${excel_table.import.insurance_new.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.insurance_new.num_insurance}")
    int numInsurance;
    @Value("${excel_table.import.insurance_new.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.insurance_new.date_end}")
    int dateEnd;
    @Value("${excel_table.import.insurance_new.sum_insured}")
    int sumInsured;
    @Value("${excel_table.import.insurance_new.date_insurance_contract}")
    int dateInsuranceContract;
    @Value("${excel_table.import.insurance_new.payment_of_insurance_premium}")
    int paymentOfInsurancePremium;
    @Value("${excel_table.import.insurance_new.franchise_amount}")
    int franchiseAmount;
}
