package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmployeeDto {

    private Long employeeId;

    private String surname;

    private String name;

    private String patronymic;

    private Long appUserId;

    private List<Long> clientsIds;

    private String fullName;

}
