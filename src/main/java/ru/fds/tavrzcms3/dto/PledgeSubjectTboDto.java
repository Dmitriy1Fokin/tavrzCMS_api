package ru.fds.tavrzcms3.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeSubjectTboDto{

    @Positive(message = "Значение должно быть больше нуля")
    private Integer countOfTBO;

    @Positive(message = "Значение должно быть больше нуля")
    private Double carryingAmount;

    private TypeOfTBO typeOfTBO;
}
