package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeAgreementDto implements Dto{

    private Long pledgeAgreementId;

    @NotBlank(message = "Обязательно для заполнения")
    private String numPA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBeginPA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    private String clientName;
}
