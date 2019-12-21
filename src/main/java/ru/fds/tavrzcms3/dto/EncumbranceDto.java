package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.TypeOfEncumbrance;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EncumbranceDto{

    private Long encumbranceId;

    @NotBlank(message = "Обязательно для заполнения")
    private String nameEncumbrance;

    private TypeOfEncumbrance typeOfEncumbrance;

    @NotBlank(message = "Обязательно для заполнения")
    private String inFavorOfWhom;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBegin;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEnd;

    @NotBlank(message = "Обязательно для заполнения")
    private String numOfEncumbrance;

    private Long pledgeSubjectId;
}
