package ru.fds.tavrzcms3.dictionary.excelproprities.pledgeagreement;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class PledgeAgreementUpdateColumn {

    @Value("${excel_table.import.pledge_agreement_update.pa_id}")
    int pledgeAgreementId;
    @Value("${excel_table.import.pledge_agreement_update.num_pa}")
    int numPA;
    @Value("${excel_table.import.pledge_agreement_update.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.pledge_agreement_update.date_end}")
    int dateEnd;
    @Value("${excel_table.import.pledge_agreement_update.perv_posl}")
    int pervPosl;
    @Value("${excel_table.import.pledge_agreement_update.status}")
    int status;
    @Value("${excel_table.import.pledge_agreement_update.client_id}")
    int clientId;
    @Value("${excel_table.import.pledge_agreement_update.la_id}")
    int loanAgreementsIds;
    @Value("${excel_table.import.pledge_agreement_update.notice}")
    int notice;
}
