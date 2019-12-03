package ru.fds.tavrzcms3.mapper;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientMapper implements Mapper<Client,ClientDto> {

    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;
    private final LoanAgreementMapper loanAgreementMapper;
    private final PledgeAgreementMapper pledgeAgreementMapper;

    public ClientMapper(ClientManagerService clientManagerService,
                        EmployeeService employeeService,
                        PledgeAgreementService pledgeAgreementService,
                        LoanAgreementService loanAgreementService,
                        LoanAgreementMapper loanAgreementMapper,
                        PledgeAgreementMapper pledgeAgreementMapper) {
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.loanAgreementMapper = loanAgreementMapper;
        this.pledgeAgreementMapper = pledgeAgreementMapper;
    }

    @Override
    public Client toEntity(ClientDto dto) {
        List<LoanAgreement> loanAgreementList = loanAgreementService.getLoanAgreementsByIds(dto.getLoanAgreementsIds());
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeAgreementsIds());

        return Client.builder()
                .clientId(dto.getClientId())
                .typeOfClient(dto.getTypeOfClient())
                .clientManager(clientManagerService.getClientManager(dto.getClientManagerId()).orElse(null))
                .employee(employeeService.getEmployeeById(dto.getEmployeeId()).orElse(null))
                .loanAgreements(loanAgreementList)
                .pledgeAgreements(pledgeAgreementList)
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

        return ClientDto.builder()
                .clientId(entity.getClientId())
                .typeOfClient(entity.getTypeOfClient())
                .clientManagerId(entity.getClientManager().getClientManagerId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .loanAgreementsIds(loanAgreementDtoList)
                .pledgeAgreementsIds(pledgeAgreementDtoList)
                .build();
    }

}
