package ru.fds.tavrzcms3.dto;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeSubjectVesselDto{

    @NotNull(message = "Обязательно для заполнения")
    private Integer imo;

    private Integer mmsi;

    @NotBlank(message = "Обязательно для заполнения")
    private String flag;

    private TypeOfVessel vesselType;

    @NotNull(message = "Обязательно для заполнения")
    @Positive(message = "Значение должно быть больше нуля")
    private int grossTonnage;

    @NotNull(message = "Обязательно для заполнения")
    @Positive(message = "Значение должно быть больше нуля")
    private int deadweight;

    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 1900, message = "Неверное значение")
    @Max(value = 2100, message = "Неверное значение")
    private int yearBuilt;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name ="status")
    private String statusVessel;
}
