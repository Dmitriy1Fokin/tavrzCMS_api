package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.*;
import ru.fds.tavrzcms3.service.*;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;

    private final DtoFactory dtoFactory;

    private final ValidatorEntity validatorEntity;

    private static final String PAGE_CARD = "client/card";
    private static final String PAGE_DETAIL = "client/detail";
    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_CLIENT = "clientDto";
    private static final String ATTR_CLIENT_MANAGER_LIST = "clientManagerDtoList";
    private static final String ATTR_EMPLOYEE_LIST = "employeeDtoList";
    private static final String ATTR_WHAT_DO = "whatDo";



    public ClientController(ClientService clientService,
                            ClientManagerService clientManagerService,
                            EmployeeService employeeService,
                            PledgeAgreementService pledgeAgreementService,
                            LoanAgreementService loanAgreementService,
                            DtoFactory dtoFactory,
                            ValidatorEntity validatorEntity) {
        this.clientService = clientService;
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/detail")
    public String clientDetailPage(@RequestParam("clientId") long clientId,
                                    Model model){

        Client client = clientService.getClientById(clientId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        ClientDto clientDto = dtoFactory.getClientDto(client);

        ClientManagerDto clientManagerDto = dtoFactory.getClientManagerDto(clientManagerService
                .getClientManager(clientDto.getClientManagerId())
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

        EmployeeDto employeeDto = dtoFactory.getEmployeeDto(employeeService
                .getEmployeeById(clientDto.getEmployeeId())
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

        List<PledgeAgreementDto> pledgeAgreementCurrentDtoList = dtoFactory
                .getPledgeAgreementsDto(pledgeAgreementService.getCurrentPledgeAgreementsByPledgor(client));

        List<PledgeAgreementDto> pledgeAgreementClosedDtoList = dtoFactory
                .getPledgeAgreementsDto(pledgeAgreementService.getClosedPledgeAgreementsByPledgor(client));

        List<LoanAgreementDto> loanAgreementCurrentDtoList = dtoFactory
                .getLoanAgreementsDto(loanAgreementService.getCurrentLoanAgreementsByLoaner(client));

        List<LoanAgreementDto> loanAgreementClosedDtoList = dtoFactory
                .getLoanAgreementsDto(loanAgreementService.getClosedLoanAgreementsByLoaner(client));

        model.addAttribute(ATTR_CLIENT, clientDto);
        model.addAttribute("clientManagerDto", clientManagerDto);
        model.addAttribute("employeeDto", employeeDto);
        model.addAttribute("pledgeAgreementCurrentList", pledgeAgreementCurrentDtoList);
        model.addAttribute("pledgeAgreementClosedList", pledgeAgreementClosedDtoList);
        model.addAttribute("loanAgreementCurrentList", loanAgreementCurrentDtoList);
        model.addAttribute("loanAgreementClosedList", loanAgreementClosedDtoList);

        return PAGE_DETAIL;
    }

    @GetMapping("/card")
    public String clientCardPage(@RequestParam("clientId") Optional<Long> clientId,
                                       @RequestParam("typeOfClient") Optional<String> typeOfClient,
                                       @RequestParam("whatDo") String whatDo,
                                       Model model){

        List<ClientManagerDto> clientManagerDtoList = dtoFactory.getClientManagersDto(clientManagerService.getAllClientManager());
        List<EmployeeDto> employeeDtoList = dtoFactory.getEmployeesDto(employeeService.getAllEmployee());

        model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
        model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
        model.addAttribute(ATTR_WHAT_DO, whatDo);

        if(whatDo.equals("updateClient")){

            Client client = clientService.getClientById(clientId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            ClientDto clientDto = dtoFactory.getClientDto(client);

            model.addAttribute(ATTR_CLIENT, clientDto);

            return PAGE_CARD;

        }else if(whatDo.equals("insertClient")){

            ClientDto clientDto = ClientDto.builder().build();
            if(typeOfClient.isPresent()){
                if(typeOfClient.get().equals(TypeOfClient.LEGAL_ENTITY.name())){
                    clientDto.setTypeOfClient(TypeOfClient.LEGAL_ENTITY);
                    clientDto.setClientLegalEntityDto(new ClientLegalEntityDto());

                }else if(typeOfClient.get().equals(TypeOfClient.INDIVIDUAL.name())){
                    clientDto.setTypeOfClient(TypeOfClient.INDIVIDUAL);
                    clientDto.setClientIndividualDto(new ClientIndividualDto());

                }
            }else {
                throw new IllegalArgumentException(MSG_WRONG_LINK);
            }


            model.addAttribute(ATTR_CLIENT, clientDto);

            return PAGE_CARD;

        }else
            throw new IllegalArgumentException(MSG_WRONG_LINK);
    }

    @PostMapping("update_insert")
    public String updateInsertClient(@Valid ClientDto clientDto,
                                     BindingResult bindingResult,
                                     @RequestParam("whatDo") String whatDo,
                                     Model model){
        if(bindingResult.hasErrors()){
            List<ClientManagerDto> clientManagerDtoList = dtoFactory.getClientManagersDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = dtoFactory.getEmployeesDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_WHAT_DO, whatDo);
            return PAGE_CARD;
        }

        Client client = dtoFactory.getClientEntity(clientDto);

        Set<ConstraintViolation<Client>> violations = validatorEntity.validateEntity(client);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        client = clientService.updateInsertClient(client);

        return clientDetailPage(client.getClientId(), model);
    }
}
