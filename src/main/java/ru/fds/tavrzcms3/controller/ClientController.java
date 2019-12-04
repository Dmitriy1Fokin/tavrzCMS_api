package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.*;
import ru.fds.tavrzcms3.mapper.*;
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

    private final ClientIndividualMapper clientIndividualMapper;
    private final ClientLegalEntityMapper clientLegalEntityMapper;
    private final EmployeeMapper employeeMapper;
    private final ClientManagerMapper clientManagerMapper;
    private final PledgeAgreementMapper pledgeAgreementMapper;
    private final LoanAgreementMapper loanAgreementMapper;

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
                            ClientIndividualMapper clientIndividualMapper,
                            ClientLegalEntityMapper clientLegalEntityMapper,
                            EmployeeMapper employeeMapper,
                            ClientManagerMapper clientManagerMapper,
                            PledgeAgreementMapper pledgeAgreementMapper,
                            LoanAgreementMapper loanAgreementMapper,
                            ValidatorEntity validatorEntity) {
        this.clientService = clientService;
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.clientIndividualMapper = clientIndividualMapper;
        this.clientLegalEntityMapper = clientLegalEntityMapper;
        this.employeeMapper = employeeMapper;
        this.clientManagerMapper = clientManagerMapper;
        this.pledgeAgreementMapper = pledgeAgreementMapper;
        this.loanAgreementMapper = loanAgreementMapper;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/detail")
    public String clientDetailPage(@RequestParam("clientId") long clientId,
                                    Model model){

        Client client = clientService.getClientById(clientId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        ClientDto clientDto;
        if(client instanceof ClientLegalEntity){
            clientDto = clientLegalEntityMapper.toDto(clientService.getClientLegalEntityByClient(client));
        }else{
            clientDto = clientIndividualMapper.toDto(clientService.getClientIndividualByClient(client));
        }

        ClientManagerDto clientManagerDto = clientManagerMapper.toDto(clientManagerService
                .getClientManager(clientDto.getClientManagerId())
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

        EmployeeDto employeeDto = employeeMapper.toDto(employeeService
                .getEmployeeById(clientDto.getEmployeeId())
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

        List<PledgeAgreementDto> pledgeAgreementCurrentDtoList = pledgeAgreementMapper
                .toDto(pledgeAgreementService.getCurrentPledgeAgreementsByPledgor(client));

        List<PledgeAgreementDto> pledgeAgreementClosedDtoList = pledgeAgreementMapper
                .toDto(pledgeAgreementService.getClosedPledgeAgreementsByPledgor(client));

        List<LoanAgreementDto> loanAgreementCurrentDtoList = loanAgreementMapper
                .toDto(loanAgreementService.getCurrentLoanAgreementsByLoaner(client));

        List<LoanAgreementDto> loanAgreementClosedDtoList = loanAgreementMapper
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
            ClientLegalEntityDto clientLegalEntityDto = clientLegalEntityMapper.toDto(clientService.getClientLegalEntityByClient(client));
            model.addAttribute(ATTR_CLIENT_LEGAL_ENTITY, clientLegalEntityDto);
        }else{
            ClientIndividualDto clientIndividualDto = clientIndividualMapper.toDto(clientService.getClientIndividualByClient(client));
            model.addAttribute(ATTR_CLIENT_INDIVIDUAL, clientIndividualDto);
        }

        List<ClientManagerDto> clientManagerDtoList = clientManagerMapper.toDto(clientManagerService.getAllClientManager());

        List<EmployeeDto> employeeDtoList = employeeMapper.toDto(employeeService.getAllEmployee());

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

        List<ClientManagerDto> clientManagerDtoList = clientManagerMapper.toDto(clientManagerService.getAllClientManager());

        List<EmployeeDto> employeeDtoList = employeeMapper.toDto(employeeService.getAllEmployee());

        model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
        model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);

        return PAGE_CARD_NEW;
    }

    @PostMapping("update_client_le")
    public String updateClientLegalEntity(@Valid ClientLegalEntityDto clientLegalEntityDto,
                                          BindingResult bindingResult,
                                          Model model){
        if(bindingResult.hasErrors()){
            List<ClientManagerDto> clientManagerDtoList = clientManagerMapper.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeMapper.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_LEGAL_ENTITY, clientLegalEntityDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntityDto.getTypeOfClient());

            return PAGE_CARD_UPDATE;
        }

        ClientLegalEntity clientLegalEntity = clientLegalEntityMapper.toEntity(clientLegalEntityDto);

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
            List<ClientManagerDto> clientManagerDtoList = clientManagerMapper.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeMapper.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_INDIVIDUAL, clientIndividualDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividualDto.getTypeOfClient());

            return PAGE_CARD_UPDATE;
        }

        ClientIndividual clientIndividual = clientIndividualMapper.toEntity(clientIndividualDto);

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
            List<ClientManagerDto> clientManagerDtoList = clientManagerMapper.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeMapper.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_LEGAL_ENTITY, clientLegalEntityDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntityDto.getTypeOfClient());

            return PAGE_CARD_NEW;
        }

        ClientLegalEntity clientLegalEntity = clientLegalEntityMapper.toEntity(clientLegalEntityDto);

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
            List<ClientManagerDto> clientManagerDtoList = clientManagerMapper.toDto(clientManagerService.getAllClientManager());
            List<EmployeeDto> employeeDtoList = employeeMapper.toDto(employeeService.getAllEmployee());
            model.addAttribute(ATTR_CLIENT_INDIVIDUAL, clientIndividualDto);
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividualDto.getTypeOfClient());

            return PAGE_CARD_NEW;
        }

        ClientIndividual clientIndividual = clientIndividualMapper.toEntity(clientIndividualDto);

        Set<ConstraintViolation<ClientIndividual>> violations = validatorEntity.validateEntity(clientIndividual);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        Client clientUpdated = clientService.updateInsertClient(clientIndividual);

        return clientDetailPage(clientUpdated.getClientId(), model);
    }
}
