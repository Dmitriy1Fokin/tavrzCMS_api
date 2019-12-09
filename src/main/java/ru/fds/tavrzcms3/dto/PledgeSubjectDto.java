package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@SuperBuilder
public class PledgeSubjectDto {

    private Long pledgeSubjectId;

    @NotBlank(message = "Обязательно для заполнения")
    private String name;

    private Liquidity liquidity;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double zsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double zsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double rsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double rsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double ss;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateMonitoring;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateConclusion;

    private StatusOfMonitoring statusMonitoring;

    private TypeOfCollateral typeOfCollateral;

    private TypeOfPledge typeOfPledge;

    private TypeOfMonitoring typeOfMonitoring;

    @NotBlank(message = "Обязательно для заполнения")
    private String adressRegion;

    private String adressDistrict;

    private String adressCity;

    private String adressStreet;

    private String adressBuilbing;

    private String adressPemises;

    @Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
    private String insuranceObligation;

    private Collection<Long> pledgeAgreementsIds;

    private Collection<Long> costHistoriesIds;

    private Collection<Long> monitoringIds;

    private Collection<Long> encumbrancesIds;

    private Collection<Long> insurancesIds;

    private String fullAddress;
}
