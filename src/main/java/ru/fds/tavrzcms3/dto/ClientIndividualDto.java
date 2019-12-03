package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ClientIndividualDto extends ClientDto{

    private String surname;

    private String name;

    private String patronymic;

    private String pasportNum;

    private String fullName;

}
