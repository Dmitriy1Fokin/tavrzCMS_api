package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.FilesService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;

    public ClientController(ClientService clientService,
                            FilesService filesService,
                            DtoFactory dtoFactory) {
        this.clientService = clientService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping
    public List<ClientDto> getAllClients(){
        return dtoFactory.getClientsDto(clientService.getAllClients());
    }

    @GetMapping("/{clientId}")
    public ClientDto getClient(@PathVariable("clientId") Long clientId){
        return clientService.getClientById(clientId)
                .map(dtoFactory::getClientDto)
                .orElseThrow(()-> new NotFoundException("Client not found"));
    }

    @GetMapping("/employee")
    public List<ClientDto> getClientsByEmployee(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getClientsDto(clientService.getClientsByEmployee(employeeId));
    }

    @GetMapping("/client_manager")
    public List<ClientDto> getClientsByClientManager(@RequestParam("clientManagerId") Long clientManagerId){
        return dtoFactory.getClientsDto(clientService.getClientsByClientManager(clientManagerId));
    }

    @GetMapping("/search")
    public List<ClientDto> getClientBySearchCriteria(@RequestParam Map<String, String> reqParam) throws ReflectiveOperationException {
        return dtoFactory.getClientsDto(clientService.getClientFromSearch(reqParam));
    }


    @PostMapping("/insert")
    public ClientDto insertClient(@Valid @RequestBody ClientDto clientDto){
        Client client = clientService.updateInsertClient(dtoFactory.getClientEntity(clientDto));

        return dtoFactory.getClientDto(client);
    }

    @PutMapping("/update")
    public ClientDto updateClient(@Valid @RequestBody ClientDto clientDto){
        return insertClient(clientDto);
    }

    @PostMapping("insert_from_file/client_legal_entity")
    public List<ClientDto> insertClientLegalEntityFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_legal_entity_new");
        List<Client> clientList = clientService.getNewClientsFromFile(uploadFile, TypeOfClient.LEGAL_ENTITY);

        return dtoFactory.getClientsDto(clientList);
    }

    @PostMapping(value = "insert_from_file/client_individual")
    public List<ClientDto> insertClientIndividualFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_individual_new");
        List<Client> clientList = clientService.getNewClientsFromFile(uploadFile, TypeOfClient.INDIVIDUAL);

        return dtoFactory.getClientsDto(clientList);
    }

    @PutMapping(value = "update_from_file/client_legal_entity")
    public List<ClientDto> updateClientLegalEntityFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_legal_entity_update");
        List<Client> clientList = clientService.getCurrentClientsFromFile(uploadFile);

        return dtoFactory.getClientsDto(clientList);
    }

    @PutMapping(value = "update_from_file/client_individual")
    public List<ClientDto> updateClientIndividualFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_individual_update");
        List<Client> clientList = clientService.getCurrentClientsFromFile(uploadFile);

        return dtoFactory.getClientsDto(clientList);
    }
}
