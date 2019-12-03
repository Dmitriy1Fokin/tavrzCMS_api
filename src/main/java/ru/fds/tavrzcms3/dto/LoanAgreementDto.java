package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class LoanAgreementDto {

    private Long loanAgreementId;

    private String numLA;

    private Date dateBeginLA;

    private Date dateEndLA;

    private StatusOfAgreement statusLA;

    private double amountLA;

    private double debtLA;

    private double interestRateLA;

    private byte pfo;

    private byte qualityCategory;

    private Long clientId;

    private List<Long> pledgeAgreementsIds;
}
