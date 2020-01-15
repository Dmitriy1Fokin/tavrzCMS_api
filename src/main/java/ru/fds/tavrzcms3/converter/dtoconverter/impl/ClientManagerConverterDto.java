package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.dto.ClientManagerDto;
import ru.fds.tavrzcms3.service.ClientService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ClientManagerConverterDto implements ConverterDto<ClientManager, ClientManagerDto> {

    private final ClientService clientService;

    public ClientManagerConverterDto(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public ClientManager toEntity(ClientManagerDto dto) {
        List<Client> clientList = Collections.emptyList();
        if(Objects.nonNull(dto.getClientsIds())){
            clientList = clientService.getClientsByIds(dto.getClientsIds());
        }

        return ClientManager.builder()
                .clientManagerId(dto.getClientManagerId())
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .clients(clientList)
                .build();
    }

    @Override
    public ClientManagerDto toDto(ClientManager entity) {
        List<Long> clientDtoList = new ArrayList<>();
        for (Client c : clientService.getAllClientsByClientManager(entity))
            clientDtoList.add(c.getClientId());

        String fullName = getFullName(entity.getSurname(), entity.getName(), entity.getPatronymic());

        return ClientManagerDto.builder()
                .clientManagerId(entity.getClientManagerId())
                .surname(entity.getSurname())
                .name(entity.getName())
                .patronymic(entity.getPatronymic())
                .clientsIds(clientDtoList)
                .fullName(fullName)
                .build();
    }

    private static String getFullName(String surname, String name, String patronymic) {
        StringBuilder fullName = new StringBuilder();
        if(Objects.nonNull(surname) && !surname.isEmpty())
            fullName.append(surname);
        if(Objects.nonNull(name) && !name.isEmpty())
            fullName.append(" ").append(name);
        if(Objects.nonNull(patronymic) && !patronymic.isEmpty())
            fullName.append(" ").append(patronymic);

        return fullName.toString();
    }
}
