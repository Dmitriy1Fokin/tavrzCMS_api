package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ValidatorEntity validatorEntity;
    private final DtoFactory dtoFactory;

    public ClientController(ClientService clientService,
                            ValidatorEntity validatorEntity,
                            DtoFactory dtoFactory) {
        this.clientService = clientService;
        this.validatorEntity = validatorEntity;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping
    public List<ClientDto> getAllClients(){
        List<Client> clientList = clientService.getAllClients() ;
        return dtoFactory.getClientsDto(clientList);
    }

    @GetMapping("/{id}")
    public ClientDto getClient(@PathVariable Long id){
        Optional<Client> client = clientService.getClientById(id);
        return client.map(dtoFactory::getClientDto)
                .orElseThrow(()-> new NullPointerException("Client not found"));
    }

    @PostMapping("/insert")
    public ClientDto insertClient(@Valid @RequestBody ClientDto clientDto){
        Client client = dtoFactory.getClientEntity(clientDto);

        Set<ConstraintViolation<Client>> violations =  validatorEntity.validateEntity(client);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        client = clientService.updateInsertClient(client);
        return dtoFactory.getClientDto(client);
    }

    @PutMapping("/update")
    public ClientDto updateClient(@Valid @RequestBody ClientDto clientDto){
        return insertClient(clientDto);
    }
}
