package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class ClientManagerDto {

    private Long clientManagerId;

    @NotBlank(message = "Обязательно для заполнения")
    private String surname;

    @NotBlank(message = "Обязательно для заполнения")
    private String name;

    private String patronymic;

    private List<Long> clientsIds;

    private String fullName;
}
