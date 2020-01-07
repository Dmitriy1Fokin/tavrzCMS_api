package ru.fds.tavrzcms3.dictionary.excelproprities.loanagreement;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class LoanAgreementUpdateColumn {
    
    @Value("${excel_table.import.loan_agreement_update.la_id}")
    int loanAgreementId;
    @Value("${excel_table.import.loan_agreement_update.num_la}")
    int numLA;
    @Value("${excel_table.import.loan_agreement_update.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.loan_agreement_update.date_end}")
    int dateEnd;
    @Value("${excel_table.import.loan_agreement_update.status}")
    int status;
    @Value("${excel_table.import.loan_agreement_update.amount}")
    int amount;
    @Value("${excel_table.import.loan_agreement_update.debt}")
    int debt;
    @Value("${excel_table.import.loan_agreement_update.interest_rate}")
    int interestRate;
    @Value("${excel_table.import.loan_agreement_update.pfo}")
    int pfo;
    @Value("${excel_table.import.loan_agreement_update.quality}")
    int quality;
    @Value("${excel_table.import.loan_agreement_update.client_id}")
    int clientId;
}
