package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;

import javax.persistence.TemporalType;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class LoanAgreementDto {

    private Long loanAgreementId;

    @NotBlank(message = "Обязательно для заполнения")
    private String numLA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBeginLA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEndLA;

    private StatusOfAgreement statusLA;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double amountLA;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double debtLA;

    @NotNull(message = "Обязательно для заполнения")
    @Positive(message = "Значение должно быть больше нуля")
    @Max(value = 1, message = "Значение не может быть больше 1")
    private double interestRateLA;

    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 1, message = "Значение должно быть от 1 до 5")
    @Max(value = 5, message = "Значение должно быть от 1 до 5")
    private byte pfo;

    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 1, message = "Значение должно быть от 1 до 5")
    @Max(value = 5, message = "Значение должно быть от 1 до 5")
    private byte qualityCategory;

    private Long clientId;

    private List<Long> pledgeAgreementsIds;
}
