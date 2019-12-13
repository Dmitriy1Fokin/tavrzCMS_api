package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class ClientLegalEntityDto extends ClientDto implements Dto{

    @NotBlank(message = "Обязательно для заполнения")
    private String organizationalForm;

    @NotBlank(message = "Обязательно для заполнения")
    private String name;

    @Pattern(regexp = "^$|[0-9]{10}",
            message = "Неверное значение")
    private String inn;
}
