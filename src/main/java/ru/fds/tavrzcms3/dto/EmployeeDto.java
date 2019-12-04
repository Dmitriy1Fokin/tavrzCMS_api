package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class EmployeeDto {

    private Long employeeId;

    @NotBlank(message = "Обязательно для заполнения")
    private String surname;

    @NotBlank(message = "Обязательно для заполнения")
    private String name;

    private String patronymic;

    private Long appUserId;

    private List<Long> clientsIds;

    private String fullName;

}
