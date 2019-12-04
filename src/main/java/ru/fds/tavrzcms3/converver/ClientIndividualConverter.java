package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.ClientIndividual;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.ClientIndividualDto;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ClientIndividualConverter implements Converter<ClientIndividual, ClientIndividualDto> {

    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;

    public ClientIndividualConverter(ClientManagerService clientManagerService,
                                     EmployeeService employeeService,
                                     PledgeAgreementService pledgeAgreementService,
                                     LoanAgreementService loanAgreementService) {
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
    }

    @Override
    public ClientIndividual toEntity(ClientIndividualDto dto) {
        List<LoanAgreement> loanAgreementList = new ArrayList<>();
        if(dto.getLoanAgreementsIds() != null)
            loanAgreementList = loanAgreementService.getLoanAgreementsByIds(dto.getLoanAgreementsIds());

        List<PledgeAgreement> pledgeAgreementList = new ArrayList<>();
        if(dto.getPledgeAgreementsIds() != null)
            pledgeAgreementList = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeAgreementsIds());

        return ClientIndividual.builder()
                .clientId(dto.getClientId())
                .typeOfClient(dto.getTypeOfClient())
                .clientManager(clientManagerService.getClientManager(dto.getClientManagerId()).orElse(null))
                .employee(employeeService.getEmployeeById(dto.getEmployeeId()).orElse(null))
                .loanAgreements(loanAgreementList)
                .pledgeAgreements(pledgeAgreementList)
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .pasportNum(dto.getPasportNum())
                .build();
    }

    @Override
    public ClientIndividualDto toDto(ClientIndividual entity) {
        List<Long> loanAgreementDtoList = new ArrayList<>();
        for (LoanAgreement la : loanAgreementService.getAllLoanAgreementsByLoaner(entity))
            loanAgreementDtoList.add(la.getLoanAgreementId());

        List<Long> pledgeAgreementDtoList = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementService.getAllPledgeAgreementsByPledgor(entity))
            pledgeAgreementDtoList.add(pa.getPledgeAgreementId());

        StringBuilder fullName = new StringBuilder();
        if(Objects.nonNull(entity.getSurname()) && !entity.getSurname().isEmpty())
            fullName.append(entity.getName());
        if(Objects.nonNull(entity.getName()) && !entity.getName().isEmpty())
            fullName.append(" " + entity.getName());
        if(Objects.nonNull(entity.getPatronymic()) && !entity.getPatronymic().isEmpty())
            fullName.append(" " + entity.getPatronymic());


        return ClientIndividualDto.builder()
                .clientId(entity.getClientId())
                .typeOfClient(entity.getTypeOfClient())
                .clientManagerId(entity.getClientManager().getClientManagerId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .loanAgreementsIds(loanAgreementDtoList)
                .pledgeAgreementsIds(pledgeAgreementDtoList)
                .surname(entity.getSurname())
                .name(entity.getName())
                .patronymic(entity.getPatronymic())
                .pasportNum(entity.getPasportNum())
                .fullName(fullName.toString())
                .build();
    }
}
