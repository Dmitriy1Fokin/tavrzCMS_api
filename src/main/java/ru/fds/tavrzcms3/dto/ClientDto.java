package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto{

    private Long clientId;

    private TypeOfClient typeOfClient;

    private Long clientManagerId;

    private Long employeeId;

    private List<Long> loanAgreementsIds;

    private List<Long> pledgeAgreementsIds;

    @Valid
    private ClientLegalEntityDto clientLegalEntityDto;

    @Valid
    private ClientIndividualDto clientIndividualDto;

    private String fullName;
}
