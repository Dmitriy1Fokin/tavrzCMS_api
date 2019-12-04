package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PledgeSubjectVesselDto extends PledgeSubjectDto{

    @Length(min = 7, max = 7, message = "Неверное значение")
    @NotNull(message = "Обязательно для заполнения")
    private Integer imo;

    @Length(min = 9, max = 9, message = "Неверное значение")
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
    @Length(min = 4, max = 4, message = "Неверное значение")
    private int yearBuilt;

    @NotBlank(message = "Обязательно для заполнения")
    @Column(name ="status")
    private String statusVessel;
}
