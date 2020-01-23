package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAgreementDto{

    private Long loanAgreementId;

    @NotBlank(message = "Обязательно для заполнения")
    private String numLA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBeginLA;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEndLA;

    private StatusOfAgreement statusLA;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private BigDecimal amountLA;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private BigDecimal debtLA;

    @NotNull(message = "Обязательно для заполнения")
    @Positive(message = "Значение должно быть больше нуля")
    @Max(value = 1, message = "Значение не может быть больше 1")
    private Double interestRateLA;

    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 1, message = "Значение должно быть от 1 до 5")
    @Max(value = 5, message = "Значение должно быть от 1 до 5")
    private Byte pfo;

    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 1, message = "Значение должно быть от 1 до 5")
    @Max(value = 5, message = "Значение должно быть от 1 до 5")
    private Byte qualityCategory;

    @NotNull(message = "Обязательно для заполнения")
    private Long clientId;
}
