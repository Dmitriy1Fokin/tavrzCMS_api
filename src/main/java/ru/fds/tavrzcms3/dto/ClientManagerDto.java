package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientManagerDto {

    private Long clientManagerId;

    private String surname;

    private String name;

    private String patronymic;

    private List<Long> clientsIds;

    private String fullName;
}
