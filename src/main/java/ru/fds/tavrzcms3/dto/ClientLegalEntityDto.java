package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ClientLegalEntityDto extends ClientDto{

    private String organizationalForm;

    private String name;

    private String inn;

    private String fullName;

}
