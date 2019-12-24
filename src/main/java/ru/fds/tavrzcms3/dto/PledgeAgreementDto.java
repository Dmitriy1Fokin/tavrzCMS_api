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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeAgreementDto{

    private Long pledgeAgreementId;

    @NotBlank(message = "Обязательно для заполнения")
    private String numPA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBeginPA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEndPA;

    private TypeOfPledgeAgreement pervPosl;

    private StatusOfAgreement statusPA;

    private String noticePA;

    private BigDecimal zsDz;

    private BigDecimal zsZz;

    private BigDecimal rsDz;

    private BigDecimal rsZz;

    private BigDecimal ss;

    private List<Long> loanAgreementsIds;

    private Long clientId;

    private List<Long> pledgeSubjectsIds;

    private List<String> briefInfoAboutCollateral;

    private List<String> typesOfCollateral;

    private List<LocalDate> datesOfConclusions;

    private List<LocalDate> datesOfMonitoring;

    private List<String> resultsOfMonitoring;

    private String clientName;
}
