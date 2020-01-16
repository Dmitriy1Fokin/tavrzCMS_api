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
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.dto.ClientManagerDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/client_manager")
public class ClientManagerController {

    private final ClientManagerService clientManagerService;
    private final FilesService filesService;
    private final ValidatorEntity validatorEntity;
    private final DtoFactory dtoFactory;

    public ClientManagerController(ClientManagerService clientManagerService,
                                   FilesService filesService,
                                   ValidatorEntity validatorEntity,
                                   DtoFactory dtoFactory) {
        this.clientManagerService = clientManagerService;
        this.filesService = filesService;
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

    @PostMapping("/insert_from_file")
    public List<ClientManagerDto> insertClientManagerFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_manager_new");
        List<ClientManager> clientManagerList = clientManagerService.getNewClientManagersFromFile(uploadFile);

        return getPersistentClientManagerDto(clientManagerList);
    }

    @PutMapping("/update_from_file")
    public List<ClientManagerDto> updateClientManagerFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_manager_update");
        List<ClientManager> clientManagerList = clientManagerService.getCurrentClientManagersFromFile(uploadFile);

        return getPersistentClientManagerDto(clientManagerList);
    }

    private List<ClientManagerDto> getPersistentClientManagerDto(List<ClientManager> clientManagerList) {
        for(int i = 0; i < clientManagerList.size(); i++){
            Set<ConstraintViolation<ClientManager>> violations =  validatorEntity.validateEntity(clientManagerList.get(i));
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + (i+1), violations);
        }

        clientManagerList = clientManagerService.updateInsertClientManagers(clientManagerList);

        return dtoFactory.getClientManagersDto(clientManagerList);
    }
}
