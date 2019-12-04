package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;

import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PledgeSubjectTboDto extends PledgeSubjectDto{

    @Positive(message = "Значение должно быть больше нуля")
    private int countOfTBO;

    @Positive(message = "Значение должно быть больше нуля")
    private double carryingAmount;

    private TypeOfTBO typeOfTBO;
}
