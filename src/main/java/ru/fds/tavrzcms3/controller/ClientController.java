package ru.fds.tavrzcms3.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final FilesService filesService;
    private final ValidatorEntity validatorEntity;
    private final DtoFactory dtoFactory;

    public ClientController(ClientService clientService,
                            FilesService filesService,
                            ValidatorEntity validatorEntity,
                            DtoFactory dtoFactory) {
        this.clientService = clientService;
        this.filesService = filesService;
        this.validatorEntity = validatorEntity;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping
    public List<ClientDto> getAllClients(){
        List<Client> clientList = clientService.getAllClients() ;

        return dtoFactory.getClientsDto(clientList);
    }

    @GetMapping("/{clientId}")
    public ClientDto getClient(@PathVariable("clientId") Long clientId){
        return clientService.getClientById(clientId)
                .map(dtoFactory::getClientDto)
                .orElseThrow(()-> new NullPointerException("Client not found"));
    }

    @GetMapping("/search")
    public List<ClientDto> getClientBySearchCriteria(@RequestParam Map<String, String> reqParam) throws ReflectiveOperationException {
        List<Client> clientList = clientService.getClientFromSearch(reqParam);

        return dtoFactory.getClientsDto(clientList);
    }


    @PostMapping("/insert")
    public ClientDto insertClient(@AuthenticationPrincipal User user,
                                  @Valid @RequestBody ClientDto clientDto){
        Client client = clientService.updateInsertClient(dtoFactory.getClientEntity(clientDto));

        return dtoFactory.getClientDto(client);
    }

    @PutMapping("/update")
    public ClientDto updateClient(@AuthenticationPrincipal User user,
                                  @Valid @RequestBody ClientDto clientDto){
        return insertClient(user, clientDto);
    }

    @PostMapping(value = "insert_from_file/client_legal_entity")
    public List<ClientDto> insertClientLegalEntityFromFile(@AuthenticationPrincipal User user,
                                                           @RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_legal_entity_new");
        List<Client> clientList = clientService.getNewClientsFromFile(uploadFile, TypeOfClient.LEGAL_ENTITY);

        return getPersistentClientsDto(clientList);
    }

    @PostMapping(value = "insert_from_file/client_individual")
    public List<ClientDto> insertClientIndividualFromFile(@AuthenticationPrincipal User user,
                                                          @RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_individual_new");
        List<Client> clientList = clientService.getNewClientsFromFile(uploadFile, TypeOfClient.INDIVIDUAL);

        return getPersistentClientsDto(clientList);
    }

    @PutMapping(value = "update_from_file/client_legal_entity")
    public List<ClientDto> updateClientLegalEntityFromFile(@AuthenticationPrincipal User user,
                                                           @RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_legal_entity_update");
        List<Client> clientList = clientService.getCurrentClientsFromFile(uploadFile);

        return getPersistentClientsDto(clientList);
    }

    @PutMapping(value = "update_from_file/client_individual")
    public List<ClientDto> updateClientIndividualFromFile(@AuthenticationPrincipal User user,
                                                          @RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_individual_update");
        List<Client> clientList = clientService.getCurrentClientsFromFile(uploadFile);

        return getPersistentClientsDto(clientList);
    }

    private List<ClientDto> getPersistentClientsDto(List<Client> clientList) {
        for(int i = 0; i < clientList.size(); i++){
            Set<ConstraintViolation<Client>> violations =  validatorEntity.validateEntity(clientList.get(i));
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + (i+1), violations);
        }

        clientList = clientService.updateInsertClients(clientList);

        return dtoFactory.getClientsDto(clientList);
    }

}
