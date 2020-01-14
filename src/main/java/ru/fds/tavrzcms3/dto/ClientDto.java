package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.validate.validationgroup.Exist;
import ru.fds.tavrzcms3.validate.validationgroup.New;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto{

    @NotNull(groups = Exist.class)
    @Null(groups = New.class)
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
