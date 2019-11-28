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
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.EmployeeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;

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
                            EmployeeService employeeService) {
        this.clientService = clientService;
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
    }

    @GetMapping("/detail")
    public String clientDetailPage(@RequestParam("clientId") long clientId,
                                    Model model){
        Client client = clientService.getClientById(clientId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_CLIENT, client);

        return PAGE_DETAIL;
    }

    @GetMapping("/card_update")
    public String clientCardUpdatePage(@RequestParam("clientId") Optional<Long> clientId,
                                       Model model){

        List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
        List<Employee> employeeList = employeeService.getAllEmployee();

        Client client = clientService.getClientById(clientId
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

        if(client.getClass()==ClientLegalEntity.class){
            model.addAttribute("clientLegalEntity", client);
        }else if(client.getClass()==ClientIndividual.class){
            model.addAttribute("clientIndividual", client);
        }

        model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerList);
        model.addAttribute(ATTR_EMPLOYEE_LIST, employeeList);
        model.addAttribute(ATTR_TYPE_OF_CLIENT, client.getTypeOfClient());

        return PAGE_CARD_UPDATE;
    }

    @GetMapping("/card_new")
    public String clientCardNewPage(@RequestParam("typeOfClient") String typeOfClient,
                                    Model model){

        List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
        List<Employee> employeeList = employeeService.getAllEmployee();

        if(typeOfClient.equals(TypeOfClient.LEGAL_ENTITY.name())){
            ClientLegalEntity clientLegalEntity = new ClientLegalEntity();
            model.addAttribute("clientLegalEntity", clientLegalEntity);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientLegalEntity.getTypeOfClient());

        }else if(typeOfClient.equals(TypeOfClient.INDIVIDUAL.name())){
            ClientIndividual clientIndividual = new ClientIndividual();
            model.addAttribute("clientIndividual", clientIndividual);
            model.addAttribute(ATTR_TYPE_OF_CLIENT, clientIndividual.getTypeOfClient());

        }

        model.addAttribute(ATTR_CLIENT_MANAGER_LIST, clientManagerList);
        model.addAttribute(ATTR_EMPLOYEE_LIST, employeeList);

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
