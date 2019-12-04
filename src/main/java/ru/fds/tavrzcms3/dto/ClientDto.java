package ru.fds.tavrzcms3.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class ClientDto{

    private Long clientId;

    private TypeOfClient typeOfClient;

    private Long clientManagerId;

    private Long employeeId;

    private List<Long> loanAgreementsIds;

    private List<Long> pledgeAgreementsIds;
}
