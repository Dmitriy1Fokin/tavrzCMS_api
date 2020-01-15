package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto{

    private Long clientId;

    @NotNull
    private TypeOfClient typeOfClient;

    @NotNull
    private Long clientManagerId;

    @NotNull
    private Long employeeId;

    @NotNull
    private List<Long> loanAgreementsIds;

    @NotNull
    private List<Long> pledgeAgreementsIds;

    @Valid
    private ClientLegalEntityDto clientLegalEntityDto;

    @Valid
    private ClientIndividualDto clientIndividualDto;

    private String fullName;
}
