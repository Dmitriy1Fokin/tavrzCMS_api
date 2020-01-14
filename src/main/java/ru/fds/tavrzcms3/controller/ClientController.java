package ru.fds.tavrzcms3.controller;

import org.springframework.validation.annotation.Validated;
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
import ru.fds.tavrzcms3.validate.validationgroup.Exist;
import ru.fds.tavrzcms3.validate.validationgroup.New;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final DtoFactory dtoFactory;

    public ClientController(ClientService clientService,
                            DtoFactory dtoFactory) {
        this.clientService = clientService;
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
        return client.map(dtoFactory::getClientDto).orElse(null);
    }

    @PostMapping("/insert")
    public ClientDto insertClient(@Validated(New.class) @RequestBody ClientDto clientDto){
        Client client = dtoFactory.getClientEntity(clientDto);
        client = clientService.updateInsertClient(client);
        return dtoFactory.getClientDto(client);
    }

    @PutMapping("/update")
    public ClientDto updateClient(@Validated(Exist.class) @RequestBody ClientDto clientDto){
        Client client = dtoFactory.getClientEntity(clientDto);
        client = clientService.updateInsertClient(client);
        return dtoFactory.getClientDto(client);
    }
}
