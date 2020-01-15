package ru.fds.tavrzcms3.controller;

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
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/client_manager")
public class ClientManagerController {

    private final ClientManagerService clientManagerService;
    private final ValidatorEntity validatorEntity;
    private final DtoFactory dtoFactory;

    public ClientManagerController(ClientManagerService clientManagerService,
                                   ValidatorEntity validatorEntity,
                                   DtoFactory dtoFactory) {
        this.clientManagerService = clientManagerService;
        this.validatorEntity = validatorEntity;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{id}")
    public ClientManagerDto getClientManager(@PathVariable Long id){
        Optional<ClientManager> clientManager = clientManagerService.getClientManagerById(id);
        return clientManager.map(dtoFactory::getClientManagerDto)
                .orElseThrow(()-> new NullPointerException("Client manager not found"));
    }

    @PostMapping("/insert")
    public ClientManagerDto insertClientManager(@Valid @RequestBody ClientManagerDto clientManagerDto){
        ClientManager clientManager = dtoFactory.getClientManagerEntity(clientManagerDto);

        Set<ConstraintViolation<ClientManager>> violations =  validatorEntity.validateEntity(clientManager);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        clientManager = clientManagerService.insertUpdateClientManager(clientManager);
        return dtoFactory.getClientManagerDto(clientManager);
    }

    @PutMapping("/update")
    public ClientManagerDto updateClientManager(@Valid @RequestBody ClientManagerDto clientManagerDto){
        return insertClientManager(clientManagerDto);
    }
}
