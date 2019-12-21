package ru.fds.tavrzcms3.converver.dtoconverter;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientIndividual;
import ru.fds.tavrzcms3.domain.ClientLegalEntity;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.ClientIndividualDto;
import ru.fds.tavrzcms3.dto.ClientLegalEntityDto;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ClientConverterDto implements ConverterDto<Client,ClientDto> {

    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;
    private final ClientService clientService;

    private final ClientIndividualConverterDto clientIndividualConverterDto;
    private final ClientLegalEntityConverterDto clientLegalEntityConverterDto;

    public ClientConverterDto(ClientManagerService clientManagerService,
                              EmployeeService employeeService,
                              PledgeAgreementService pledgeAgreementService,
                              LoanAgreementService loanAgreementService,
                              ClientService clientService,
                              ClientIndividualConverterDto clientIndividualConverterDto,
                              ClientLegalEntityConverterDto clientLegalEntityConverterDto) {
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.clientService = clientService;
        this.clientIndividualConverterDto = clientIndividualConverterDto;
        this.clientLegalEntityConverterDto = clientLegalEntityConverterDto;
    }

    @Override
    public Client toEntity(ClientDto dto) {
        List<LoanAgreement> loanAgreementList;
        if(Objects.nonNull(dto.getLoanAgreementsIds()))
            loanAgreementList = loanAgreementService.getLoanAgreementsByIds(dto.getLoanAgreementsIds());
        else
            loanAgreementList = Collections.emptyList();

        List<PledgeAgreement> pledgeAgreementList;
        if(Objects.nonNull(dto.getPledgeAgreementsIds()))
            pledgeAgreementList = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeAgreementsIds());
        else
            pledgeAgreementList = Collections.emptyList();

        ClientIndividual clientIndividual = null;
        ClientLegalEntity clientLegalEntity = null;

        if(Objects.nonNull(dto.getClientIndividualDto()))
            clientIndividual = clientIndividualConverterDto.toEntity(dto.getClientIndividualDto());
        if(Objects.nonNull(dto.getClientLegalEntityDto()))
            clientLegalEntity = clientLegalEntityConverterDto.toEntity(dto.getClientLegalEntityDto());

        return Client.builder()
                .clientId(dto.getClientId())
                .typeOfClient(dto.getTypeOfClient())
                .clientManager(clientManagerService.getClientManager(dto.getClientManagerId()).orElse(null))
                .employee(employeeService.getEmployeeById(dto.getEmployeeId()).orElse(null))
                .loanAgreements(loanAgreementList)
                .pledgeAgreements(pledgeAgreementList)
                .clientIndividual(clientIndividual)
                .clientLegalEntity(clientLegalEntity)
                .build();
    }

    @Override
    public ClientDto toDto(Client entity) {
        List<Long> loanAgreementDtoList = new ArrayList<>();
        for (LoanAgreement la : loanAgreementService.getAllLoanAgreementsByLoaner(entity))
            loanAgreementDtoList.add(la.getLoanAgreementId());

        List<Long> pledgeAgreementDtoList = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementService.getAllPledgeAgreementsByPledgor(entity))
            pledgeAgreementDtoList.add(pa.getPledgeAgreementId());

        ClientIndividualDto clientIndividualDto = null;
        ClientLegalEntityDto clientLegalEntityDto = null;

        if(Objects.nonNull(entity.getClientIndividual()))
            clientIndividualDto = clientIndividualConverterDto.toDto(entity.getClientIndividual());
        if(Objects.nonNull(entity.getClientLegalEntity()))
            clientLegalEntityDto = clientLegalEntityConverterDto.toDto(entity.getClientLegalEntity());

        return ClientDto.builder()
                .clientId(entity.getClientId())
                .typeOfClient(entity.getTypeOfClient())
                .clientManagerId(entity.getClientManager().getClientManagerId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .loanAgreementsIds(loanAgreementDtoList)
                .pledgeAgreementsIds(pledgeAgreementDtoList)
                .clientIndividualDto(clientIndividualDto)
                .clientLegalEntityDto(clientLegalEntityDto)
                .fullName(clientService.getFullNameClient(entity.getClientId()))
                .build();
    }
}
