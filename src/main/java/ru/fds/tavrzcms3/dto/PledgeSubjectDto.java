package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.Liquidity;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfPledge;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeSubjectDto{

    private Long pledgeSubjectId;

    @NotBlank(message = "Обязательно для заполнения")
    private String name;

    private Liquidity liquidity;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private BigDecimal zsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private BigDecimal zsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private BigDecimal rsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private BigDecimal rsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private BigDecimal ss;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateMonitoring;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateConclusion;

    @NotNull(message = "Обязательно для заполнения")
    private StatusOfMonitoring statusMonitoring;

    @NotNull(message = "Обязательно для заполнения")
    private TypeOfCollateral typeOfCollateral;

    @NotNull(message = "Обязательно для заполнения")
    private TypeOfPledge typeOfPledge;

    @NotNull(message = "Обязательно для заполнения")
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

    private String fullAddress;

    private MainCharacteristic mainCharacteristic;

    private PrimaryIdentifier primaryIdentifier;

    @Valid
    private PledgeSubjectAutoDto pledgeSubjectAutoDto;

    @Valid
    private PledgeSubjectEquipmentDto pledgeSubjectEquipmentDto;

    @Valid
    private PledgeSubjectBuildingDto pledgeSubjectBuildingDto;

    @Valid
    private PledgeSubjectLandLeaseDto pledgeSubjectLandLeaseDto;

    @Valid
    private PledgeSubjectLandOwnershipDto pledgeSubjectLandOwnershipDto;

    @Valid
    private PledgeSubjectRoomDto pledgeSubjectRoomDto;

    @Valid
    private PledgeSubjectSecuritiesDto pledgeSubjectSecuritiesDto;

    @Valid
    private PledgeSubjectTboDto pledgeSubjectTboDto;

    @Valid
    private PledgeSubjectVesselDto pledgeSubjectVesselDto;
}
