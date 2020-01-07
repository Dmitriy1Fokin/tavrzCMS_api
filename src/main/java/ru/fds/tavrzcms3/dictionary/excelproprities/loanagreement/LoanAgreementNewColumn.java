package ru.fds.tavrzcms3.dictionary.excelproprities.loanagreement;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class LoanAgreementNewColumn {

    @Value("${excel_table.import.loan_agreement_new.num_la}")
    int numLA;
    @Value("${excel_table.import.loan_agreement_new.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.loan_agreement_new.date_end}")
    int dateEnd;
    @Value("${excel_table.import.loan_agreement_new.status}")
    int status;
    @Value("${excel_table.import.loan_agreement_new.amount}")
    int amount;
    @Value("${excel_table.import.loan_agreement_new.debt}")
    int debt;
    @Value("${excel_table.import.loan_agreement_new.interest_rate}")
    int interestRate;
    @Value("${excel_table.import.loan_agreement_new.pfo}")
    int pfo;
    @Value("${excel_table.import.loan_agreement_new.quality}")
    int quality;
    @Value("${excel_table.import.loan_agreement_new.client_id}")
    int clientId;

}
