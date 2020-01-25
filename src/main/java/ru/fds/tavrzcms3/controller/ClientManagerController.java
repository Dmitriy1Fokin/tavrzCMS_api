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
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.FilesService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/client_manager")
public class ClientManagerController {

    private final ClientManagerService clientManagerService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;

    public ClientManagerController(ClientManagerService clientManagerService,
                                   FilesService filesService,
                                   DtoFactory dtoFactory) {
        this.clientManagerService = clientManagerService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{clientManagerId}")
    public ClientManagerDto getClientManager(@PathVariable("clientManagerId") Long clientManagerId){
        return clientManagerService.getClientManagerById(clientManagerId).map(dtoFactory::getClientManagerDto)
                .orElseThrow(()-> new NotFoundException("Client manager not found"));
    }

    @GetMapping("/all")
    public List<ClientManagerDto> getAllClientManagers(){
        return dtoFactory.getClientManagersDto(clientManagerService.getAllClientManager());
    }

    @GetMapping("/client")
    public ClientManagerDto getClientManagerByClient(@RequestParam("clientId") Long clientId){
        return dtoFactory.getClientManagerDto(clientManagerService.getClientManagerByClient(clientId)
                .orElseThrow(()-> new NotFoundException("Client manager not found")));
    }

    @PostMapping("/insert")
    public ClientManagerDto insertClientManager(@Valid @RequestBody ClientManagerDto clientManagerDto){
        ClientManager clientManager = clientManagerService
                .insertUpdateClientManager(dtoFactory.getClientManagerEntity(clientManagerDto));

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

        return dtoFactory.getClientManagersDto(clientManagerList);
    }

    @PutMapping("/update_from_file")
    public List<ClientManagerDto> updateClientManagerFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "client_manager_update");
        List<ClientManager> clientManagerList = clientManagerService.getCurrentClientManagersFromFile(uploadFile);

        return dtoFactory.getClientManagersDto(clientManagerList);
    }
}
