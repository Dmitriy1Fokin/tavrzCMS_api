package ru.fds.tavrzcms3.domain.embed;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Data
@Embeddable
public class PersonContact {

    @NotBlank(message = "Обязательно для заполнения")
    private String surname;
    @NotBlank(message = "Обязательно для заполнения")
    private String name;

    private String patronymic;

}
