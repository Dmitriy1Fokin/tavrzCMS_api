package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PledgeAgreementDto {

    private Long pledgeAgreementId;

    private String numPA;

    private Date dateBeginPA;

    private Date dateEndPA;

    private TypeOfPledgeAgreement pervPosl;

    private StatusOfAgreement statusPA;

    private String noticePA;

    private double zsDz;

    private double zsZz;

    private double rsDz;

    private double rsZz;

    private double ss;

    private List<Long> loanAgreementsIds;

    private Long clientId;

    private List<Long> pledgeSubjectsIds;

    private List<String> briefInfoAboutCollateral;

    private List<String> typesOfCollateral;

    private List<Date> datesOfConclusions;

    private List<Date> datesOfMonitoring;

    private List<String> resultsOfMonitoring;
}
