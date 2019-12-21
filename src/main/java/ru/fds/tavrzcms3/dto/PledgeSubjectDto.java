package ru.fds.tavrzcms3.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private LocalDate dateMonitoring;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateConclusion;

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


    @Singular
    private List<Long> pledgeAgreementsIds;

    @Singular
    private List<Long> costHistoriesIds;

    @Singular
    private List<Long> monitoringIds;

    @Singular
    private List<Long> encumbrancesIds;

    @Singular
    private List<Long> insurancesIds;

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
