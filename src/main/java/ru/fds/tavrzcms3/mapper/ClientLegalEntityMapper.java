package ru.fds.tavrzcms3.mapper;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.ClientLegalEntity;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.ClientLegalEntityDto;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientLegalEntityMapper implements Mapper<ClientLegalEntity, ClientLegalEntityDto> {

    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;

    public ClientLegalEntityMapper(ClientManagerService clientManagerService,
                                   EmployeeService employeeService,
                                   PledgeAgreementService pledgeAgreementService,
                                   LoanAgreementService loanAgreementService) {
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
    }

    @Override
    public ClientLegalEntity toEntity(ClientLegalEntityDto dto) {
        List<LoanAgreement> loanAgreementList = new ArrayList<>();
        if(dto.getLoanAgreementsIds() != null)
            loanAgreementList = loanAgreementService.getLoanAgreementsByIds(dto.getLoanAgreementsIds());

        List<PledgeAgreement> pledgeAgreementList = new ArrayList<>();
        if(dto.getPledgeAgreementsIds() != null)
                pledgeAgreementList = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeAgreementsIds());

        return ClientLegalEntity.builder()
                .clientId(dto.getClientId())
                .typeOfClient(dto.getTypeOfClient())
                .clientManager(clientManagerService.getClientManager(dto.getClientManagerId()).orElse(null))
                .employee(employeeService.getEmployeeById(dto.getEmployeeId()).orElse(null))
                .loanAgreements(loanAgreementList)
                .pledgeAgreements(pledgeAgreementList)
                .organizationalForm(dto.getOrganizationalForm())
                .name(dto.getName())
                .inn(dto.getInn())
                .build();
    }

    @Override
    public ClientLegalEntityDto toDto(ClientLegalEntity entity) {
        List<Long> loanAgreementDtoList = new ArrayList<>();
        for (LoanAgreement la : loanAgreementService.getAllLoanAgreementsByLoaner(entity))
            loanAgreementDtoList.add(la.getLoanAgreementId());

        List<Long> pledgeAgreementDtoList = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementService.getAllPledgeAgreementsByPledgor(entity))
            pledgeAgreementDtoList.add(pa.getPledgeAgreementId());

        return ClientLegalEntityDto.builder()
                .clientId(entity.getClientId())
                .typeOfClient(entity.getTypeOfClient())
                .clientManagerId(entity.getClientManager().getClientManagerId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .loanAgreementsIds(loanAgreementDtoList)
                .pledgeAgreementsIds(pledgeAgreementDtoList)
                .organizationalForm(entity.getOrganizationalForm())
                .name(entity.getName())
                .inn(entity.getInn())
                .fullName(entity.getOrganizationalForm() + "\"" + entity.getName() + "\"")
                .build();
    }
}
