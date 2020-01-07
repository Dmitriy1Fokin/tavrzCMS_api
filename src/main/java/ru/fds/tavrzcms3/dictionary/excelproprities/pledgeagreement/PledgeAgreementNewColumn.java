package ru.fds.tavrzcms3.dictionary.excelproprities.pledgeagreement;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class PledgeAgreementNewColumn {
    
    @Value("${excel_table.import.pledge_agreement_new.num_pa}")
    int numPA;
    @Value("${excel_table.import.pledge_agreement_new.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.pledge_agreement_new.date_end}")
    int dateEnd;
    @Value("${excel_table.import.pledge_agreement_new.perv_posl}")
    int pervPosl;
    @Value("${excel_table.import.pledge_agreement_new.status}")
    int status;
    @Value("${excel_table.import.pledge_agreement_new.client_id}")
    int clientId;
    @Value("${excel_table.import.pledge_agreement_new.la_id}")
    int loanAgreementsIds;
    @Value("${excel_table.import.pledge_agreement_new.notice}")
    int notice;
}
