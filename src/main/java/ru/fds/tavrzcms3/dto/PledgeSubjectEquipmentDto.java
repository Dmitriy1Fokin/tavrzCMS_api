package ru.fds.tavrzcms3.dto;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.TypeOfEquipment;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeSubjectEquipmentDto{

    @NotBlank(message = "Обязательно для заполнения")
    private String brand;

    @NotBlank(message = "Обязательно для заполнения")
    private String model;

    private String serialNum;

    @Min(value = 1900, message = "Неверное значение")
    @Max(value = 2100, message = "Неверное значение")
    private Integer yearOfManufacture;

    private String inventoryNum;

    private TypeOfEquipment typeOfEquipment;

    @Positive(message = "Значение должно быть больше нуля")
    private Double productivity;

    private String typeOfProductivity;
}
