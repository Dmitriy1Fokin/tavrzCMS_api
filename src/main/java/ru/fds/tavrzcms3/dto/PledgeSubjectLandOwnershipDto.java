package ru.fds.tavrzcms3.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.domain.LandCategory;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeSubjectLandOwnershipDto implements Dto{

    @Valid
    private PledgeSubjectDto pledgeSubjectDto;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    private double area;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    private String cadastralNum;

    private String conditionalNum;

    @NotBlank(message = "Обязательно для заполнения")
    private String permittedUse;

    @Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
    private String builtUp;

    @Pattern(regexp = "^$|([0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+(( *; *[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+)|($))*)+",
            message = "Неверное значение. Если несколько кад№, указывать через \";\"")
    private String cadastralNumOfBuilding;

    private LandCategory landCategory;
}
