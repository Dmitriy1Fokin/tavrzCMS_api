package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.fds.tavrzcms3.dictionary.TypeOfEquip;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PledgeSubjectEquipmentDto extends PledgeSubjectDto{

    @NotBlank(message = "Обязательно для заполнения")
    private String brand;

    @NotBlank(message = "Обязательно для заполнения")
    private String model;

    private String serialNum;

    @Min(value = 1900, message = "Неверное значение")
    @Max(value = 2100, message = "Неверное значение")
    @Length(min = 4, max = 4, message = "Неверное значение")
    private Integer yearOfManufacture;

    private String inventoryNum;

    private TypeOfEquip typeOfEquipment;

    @Positive(message = "Значение должно быть больше нуля")
    private Double productivity;

    private String typeOfProductivity;
}
