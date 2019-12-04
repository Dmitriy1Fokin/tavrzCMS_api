package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.*;
import ru.fds.tavrzcms3.converver.*;
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

    private final ClientIndividualConverter clientIndividualConverter;
    private final ClientLegalEntityConverter clientLegalEntityConverter;
    private final EmployeeConverter employeeConverter;
    private final ClientManagerConverter clientManagerConverter;
    private final PledgeAgreementConverter pledgeAgreementConverter;
    private final LoanAgreementConverter loanAgreementConverter;

    private final ValidatorEntity validatorEntity;

    private static final String PAGE_CARD_UPDATE = "client/card_update";
    private static final String PAGE_CARD_NEW = "client/card_new";
    private static final String PAGE_DETAIL = "client/detail";
    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_CLIENT = "client";
    private static final String ATTR_CLIENT_LEGAL_ENTITY = "clientLegalEntityDto";
    private static final String ATTR_CLIENT_INDIVIDUAL = "clientIndividualDto";
    private static final String ATTR_CLIENT_MANAGER_LIST = "clientManagerList";
    private static final String ATTR_EMPLOYEE_LIST = "employeeList";
    private static final String ATTR_TYPE_OF_CLIENT = "typeOfClient";



    public ClientController(ClientService clientService,
                            ClientManagerService clientManagerService,
                            EmployeeService employeeService,
                            PledgeAgreementService pledgeAgreementService,
                            LoanAgreementService loanAgreementService,
                            ClientIndividualConverter clientIndividualConverter,
                            ClientLegalEntityConverter clientLegalEntityConverter,
                            EmployeeConverter employeeConverter,
                            ClientManagerConverter clientManagerConverter,
                            PledgeAgreementConverter pledgeAgreementConverter,
                            LoanAgreementConverter loanAgreementConverter,
                            ValidatorEntity validatorEntity) {
        this.clientService = clientService;
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.clientIndividualConverter = clientIndividualConverter;
        this.clientLegalEntityConverter = clientLegalEntityConverter;
        this.employeeConverter = employeeConverter;
        this.clientManagerConverter = clientManagerConverter;
        this.pledgeAgreementConverter = pledgeAgreementConverter;
        this.loanAgreementConverter = loanAgreementConverter;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/detail")
    public String clientDetailPage(@RequestParam("clientId") long clientId,
                                    Model model){

        Client client = clientService.getClientById(clientId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        ClientDto clientDto;
        if(client instanceof ClientLegalEntity){
            clientDto = clientLegalEntityConverter.toDto(clientService.getClientLegalEntityByClient(client));
        }else{
            clientDto = clientIndividualConverter.toDto(clientService.getClientIndividualByClient(client));
        }

        ClientManagerDto clientManagerDto = clientManagerConverter.toDto(clientManagerService
                .getClientManager(clientDto.getClientManagerId())
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

        EmployeeDto employeeDto = employeeConverter.toDto(employeeService
                .getEmployeeById(clientDto.getEmployeeId())
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

        List<PledgeAgreementDto> pledgeAgreementCurrentDtoList = pledgeAgreementConverter
                .toDto(pledgeAgreementService.getCurrentPledgeAgreementsByPledgor(client));

        List<PledgeAgreementDto> pledgeAgreementClosedDtoList = pledgeAgreementConverter
                .toDto(pledgeAgreementService.getClosedPledgeAgreementsByPledgor(client));

        List<LoanAgreementDto> loanAgreementCurrentDtoList = loanAgreementConverter
                .toDto(loanAgreementService.getCurrentLoanAgreementsByLoaner(client));

        List<LoanAgreementDto> loanAgreementClosedDtoList = loanAgreementConverter
                .toDto(loanAgreementService.getClosedLoanAgreementsByLoaner(client));

        model.addAttribute(ATTR_CLIENT, clientDto);
        model.addAttribute("clientManager", clientManagerDto);
        model.addAttribute("employee", employeeDto);
        model.addAttribute("pledgeAgreementCurrentList", pledgeAgreementCurrentDtoList);
        model.addAttribute("pledgeAgreementClosedList", pledgeAgreementClosedDtoList);
        model.addAttribute("loanAgreementCurrentList", loanAgreementCurrentDtoList);
        model.addAttribute("loanAgreementClosedList", loanAgreementClosedDtoList);

        return PAGE_DETAIL;
    }

    @GetMapping("/client_card_update")
    public String clientCardUpdatePage(@RequestParam("clientId") Optional<Long> clientId,
                                       Model model){

        Client client = clientService.getClientById(clientId
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

        if(client instanceof ClientLegalEntity){
            ClientLegalEntityDto clientLegalEntityDto = clientLegalEntityConverter.toDto(clientService.getClientLegalEntityByClient(client));
            model.addAttribute(ATTR_CLIENT_LEGAL_ENTITY, clientLegalEntityDto);
        }else{
            ClientIndividualDto clientIndividualDto = clientIndividualConverter.toDto(clientService.getClientIndividualByClient(client));
            model.addAttribute(ATTR_CLIENT_INDIVIDUAL, clientIndividualDto);
        }

        List<ClientManagerDto> clientManagerDtoList = clientManagerConverter.toDto(clientManagerService.getAllClientManager());

        List<EmployeeDto> employeeDtoList = employeeConverter.toDto(employeeService.getAllEmployee());

        model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
        model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
        model.addAttribute(ATTR_TYPE_OF_CLIENT, client.getTypeOfClient());

        return PAGE_CARD_UPDATE;
    }

    @GetMapping("/card_new")
    public String clientCardNewPage(@RequestParam("typeOfClient") String typeOfClient,
                                    Model model){

        if(typeOfClient.equals(TypeOfClient.LEGAL_ENTITY.name())){
            ClientLegalEntityDto clientLegalEntityDto = ClientLegalEntityDto.builder()
                    .typeOfClient(TypeOfClient.LEGAL_ENTITY)
                    .build();
            model.addAttribute(ATTR_CLIENT_LEGAL_ENTITY, clientLegalEntityDto);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntityDto.getTypeOfClient());

        }else if(typeOfClient.equals(TypeOfClient.INDIVIDUAL.name())){
            ClientIndividualDto clientIndividualDto = ClientIndividualDto.builder()
                    .typeOfClient(TypeOfClient.INDIVIDUAL)
                    .build();
            model.addAttribute(ATTR_CLIENT_INDIVIDUAL, clientIndividualDto);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividualDto.getTypeOfClient());

        }

        List<ClientManagerDto> clientManagerDtoList = clientManagerConverter.toDto(clientManagerService.getAllClientManager());

        List<EmployeeDto> employeeDtoList = employeeConverter.toDto(employeeService.getAllEmployee());

        model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
        model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);

        return PAGE_CARD_NEW;
    }

    @PostMapping("update_client_le")
    public String updateClientLegalEntity(@Valid ClientLegalEntityDto clientLegalEntityDto,
                                          BindingResult bindingResult,
                                          Model model){
        if(bindingResult.hasErrors()){
            List<ClientManagerDto> clientManagerDtoList = clientManagerConverter.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeConverter.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_LEGAL_ENTITY, clientLegalEntityDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntityDto.getTypeOfClient());

            return PAGE_CARD_UPDATE;
        }

        ClientLegalEntity clientLegalEntity = clientLegalEntityConverter.toEntity(clientLegalEntityDto);

        Set<ConstraintViolation<ClientLegalEntity>> violations = validatorEntity.validateEntity(clientLegalEntity);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        Client clientUpdated = clientService.updateInsertClient(clientLegalEntity);

        return clientDetailPage(clientUpdated.getClientId(), model);
    }

    @PostMapping("update_client_ind")
    public String updateClientIndividual(@Valid ClientIndividualDto clientIndividualDto,
                                         BindingResult bindingResult,
                                         Model model){
        if(bindingResult.hasErrors()){
            List<ClientManagerDto> clientManagerDtoList = clientManagerConverter.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeConverter.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_INDIVIDUAL, clientIndividualDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividualDto.getTypeOfClient());

            return PAGE_CARD_UPDATE;
        }

        ClientIndividual clientIndividual = clientIndividualConverter.toEntity(clientIndividualDto);

        Set<ConstraintViolation<ClientIndividual>> violations =  validatorEntity.validateEntity(clientIndividual);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        Client clientUpdated = clientService.updateInsertClient(clientIndividual);

        return clientDetailPage(clientUpdated.getClientId(), model);
    }

    @PostMapping("insert_client_le")
    public String insertNewClientLegalEntity(@Valid ClientLegalEntityDto clientLegalEntityDto,
                                             BindingResult bindingResult,
                                             Model model){
        if(bindingResult.hasErrors()){
            List<ClientManagerDto> clientManagerDtoList = clientManagerConverter.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeConverter.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_LEGAL_ENTITY, clientLegalEntityDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntityDto.getTypeOfClient());

            return PAGE_CARD_NEW;
        }

        ClientLegalEntity clientLegalEntity = clientLegalEntityConverter.toEntity(clientLegalEntityDto);

        Set<ConstraintViolation<ClientLegalEntity>> violations = validatorEntity.validateEntity(clientLegalEntity);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        Client clientUpdated = clientService.updateInsertClient(clientLegalEntity);

        return clientDetailPage(clientUpdated.getClientId(), model);
    }

    @PostMapping("insert_client_ind")
    public String insertNewClientIndividual(@Valid ClientIndividualDto clientIndividualDto,
                                            BindingResult bindingResult,
                                            Model model){
        if(bindingResult.hasErrors()){
            List<ClientManagerDto> clientManagerDtoList = clientManagerConverter.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeConverter.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_INDIVIDUAL, clientIndividualDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividualDto.getTypeOfClient());

            return PAGE_CARD_NEW;
        }

        ClientIndividual clientIndividual = clientIndividualConverter.toEntity(clientIndividualDto);

        Set<ConstraintViolation<ClientIndividual>> violations = validatorEntity.validateEntity(clientIndividual);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        Client clientUpdated = clientService.updateInsertClient(clientIndividual);

        return clientDetailPage(clientUpdated.getClientId(), model);
    }
}
