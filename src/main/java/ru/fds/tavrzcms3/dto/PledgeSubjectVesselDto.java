package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeSubjectVesselDto{

    @Pattern(regexp = "[0-9]{7}")
    @NotNull(message = "Обязательно для заполнения")
    private String imo;

    @Pattern(regexp = "^$|[0-9]{9}")
    private String mmsi;

    @NotBlank(message = "Обязательно для заполнения")
    private String flag;

    private TypeOfVessel vesselType;

    @NotNull(message = "Обязательно для заполнения")
    @Positive(message = "Значение должно быть больше нуля")
    private Integer grossTonnage;

    @NotNull(message = "Обязательно для заполнения")
    @Positive(message = "Значение должно быть больше нуля")
    private Integer deadweight;

    @NotNull(message = "Обязательно для заполнения")
    @Min(value = 1900, message = "Неверное значение")
    @Max(value = 2100, message = "Неверное значение")
    private Integer yearBuilt;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name ="status")
    private String statusVessel;
}
