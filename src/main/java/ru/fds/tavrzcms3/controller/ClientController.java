package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.*;
import ru.fds.tavrzcms3.mapper.*;
import ru.fds.tavrzcms3.service.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    private static final String PAGE_CARD_UPDATE = "client/card_update";
    private static final String PAGE_CARD_NEW = "client/card_new";
    private static final String PAGE_DETAIL = "client/detail";
    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_CLIENT = "client";
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
                            LoanAgreementMapper loanAgreementMapper) {
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

    @GetMapping("/card_update")
    public String clientCardUpdatePage(@RequestParam("clientId") Optional<Long> clientId,
                                       Model model){

        Client client = clientService.getClientById(clientId
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

        if(client instanceof ClientLegalEntity){
            ClientLegalEntityDto clientLegalEntityDto = clientLegalEntityMapper.toDto(clientService.getClientLegalEntityByClient(client));
            model.addAttribute("clientLegalEntity", clientLegalEntityDto);
        }else{
            ClientIndividualDto clientIndividualDto = clientIndividualMapper.toDto(clientService.getClientIndividualByClient(client));
            model.addAttribute("clientIndividual", clientIndividualDto);
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
//            ClientLegalEntity clientLegalEntity = new ClientLegalEntity();
            model.addAttribute("clientLegalEntity", clientLegalEntityDto);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntityDto.getTypeOfClient());

        }else if(typeOfClient.equals(TypeOfClient.INDIVIDUAL.name())){
            ClientIndividualDto clientIndividualDto = ClientIndividualDto.builder()
                    .typeOfClient(TypeOfClient.INDIVIDUAL)
                    .build();
//            ClientIndividual clientIndividual = new ClientIndividual();
            model.addAttribute("clientIndividual", clientIndividualDto);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividualDto.getTypeOfClient());

        }

        List<ClientManagerDto> clientManagerDtoList = clientManagerMapper.toDto(clientManagerService.getAllClientManager());

        List<EmployeeDto> employeeDtoList = employeeMapper.toDto(employeeService.getAllEmployee());

        model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerDtoList);
        model.addAttribute(ATTR_EMPLOYEE_LIST, employeeDtoList);

        return PAGE_CARD_NEW;
    }

    @PostMapping("update_client_le")
    public String updateClientLegalEntity(@Valid ClientLegalEntity clientLegalEntity,
                                          BindingResult bindingResult,
                                          Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntity.getTypeOfClient());

            return PAGE_CARD_UPDATE;
        }

        Client clientUpdated = clientService.updateInsertClient(clientLegalEntity);

        model.addAttribute(ATTR_CLIENT, clientUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_client_ind")
    public String updateClientIndividual(@Valid ClientIndividual clientIndividual,
                                         BindingResult bindingResult,
                                         Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividual.getTypeOfClient());

            return PAGE_CARD_UPDATE;
        }

        Client clientUpdated = clientService.updateInsertClient(clientIndividual);
        model.addAttribute(ATTR_CLIENT, clientUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("insert_client_le")
    public String insertNewClientLegalEntity(@Valid ClientLegalEntity clientLegalEntity,
                                             BindingResult bindingResult,
                                             Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntity.getTypeOfClient());

            return PAGE_CARD_NEW;
        }

        Client clientUpdated = clientService.updateInsertClient(clientLegalEntity);

        model.addAttribute(ATTR_CLIENT, clientUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("insert_client_ind")
    public String insertNewClientIndividual(@Valid ClientIndividual clientIndividual,
                                            BindingResult bindingResult,
                                            Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerList);
            model.addAttribute(ATTR_EMPLOYEE_LIST, employeeList);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividual.getTypeOfClient());

            return PAGE_CARD_NEW;
        }

        Client clientUpdated = clientService.updateInsertClient(clientIndividual);
        model.addAttribute(ATTR_CLIENT, clientUpdated);

        return PAGE_DETAIL;
    }
}
