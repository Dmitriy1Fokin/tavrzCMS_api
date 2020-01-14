package ru.fds.tavrzcms3.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.dto.ClientManagerDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.validate.validationgroup.Exist;
import ru.fds.tavrzcms3.validate.validationgroup.New;

import java.util.Optional;

@RestController
@RequestMapping("/client_manager")
public class ClientManagerController {

    private final ClientManagerService clientManagerService;
    private final DtoFactory dtoFactory;

    public ClientManagerController(ClientManagerService clientManagerService,
                                   DtoFactory dtoFactory) {
        this.clientManagerService = clientManagerService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{id}")
    public ClientManagerDto getClientManager(@PathVariable Long id){
        Optional<ClientManager> clientManager = clientManagerService.getClientManagerById(id);
        return clientManager.map(dtoFactory::getClientManagerDto).orElse(null);
    }

    @PostMapping("/insert")
    public ClientManagerDto insertClientManager(@Validated(New.class) @RequestBody ClientManagerDto clientManagerDto){
        ClientManager clientManager = dtoFactory.getClientManagerEntity(clientManagerDto);
        clientManager = clientManagerService.insertUpdateClientManager(clientManager);
        return dtoFactory.getClientManagerDto(clientManager);
    }

    @PutMapping("/update")
    public ClientManagerDto updateClientManager(@Validated(Exist.class) @RequestBody ClientManagerDto clientManagerDto){
        return insertClientManager(clientManagerDto);
    }
}
