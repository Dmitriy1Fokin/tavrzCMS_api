package ru.fds.tavrzcms3.dto;

import lombok.*;
//import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Length;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeSubjectAutoDto implements Dto {

    @Valid
    private PledgeSubjectDto pledgeSubjectDto;

    @NotBlank(message = "Обязательно для заполнения")
    private String brand;

    @NotBlank(message = "Обязательно для заполнения")
    private String model;

    @Pattern(regexp = "^$|[0-9A-Z]{17}",
            message = "Неверное значение")
    private String vin;

    private String numOfEngine;

    private String numOfPTS;

    @Min(value = 1900, message = "Неверное значение")
    @Max(value = 2100, message = "Неверное значение")
//    @Length(min = 4, max = 4, message = "Неверное значение")
    private Integer yearOfManufacture;

    private String inventoryNum;

    private TypeOfAuto typeOfAuto;

    @Positive(message = "Значение должно быть больше нуля")
    private Double horsepower;

}
