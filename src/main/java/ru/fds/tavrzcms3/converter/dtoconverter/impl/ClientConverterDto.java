package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.ClientIndividualDto;
import ru.fds.tavrzcms3.dto.ClientLegalEntityDto;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.service.EmployeeService;

import java.util.Objects;

@Component
public class ClientConverterDto implements ConverterDto<Client,ClientDto> {

    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;

    private final ClientIndividualConverterDto clientIndividualConverterDto;
    private final ClientLegalEntityConverterDto clientLegalEntityConverterDto;

    public ClientConverterDto(ClientManagerService clientManagerService,
                              EmployeeService employeeService,
                              ClientIndividualConverterDto clientIndividualConverterDto,
                              ClientLegalEntityConverterDto clientLegalEntityConverterDto) {
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.clientIndividualConverterDto = clientIndividualConverterDto;
        this.clientLegalEntityConverterDto = clientLegalEntityConverterDto;
    }

    @Override
    public Client toEntity(ClientDto dto) {
        ClientIndividual clientIndividual = null;
        ClientLegalEntity clientLegalEntity = null;

        if(Objects.nonNull(dto.getClientIndividualDto()))
            clientIndividual = clientIndividualConverterDto.toEntity(dto.getClientIndividualDto());
        if(Objects.nonNull(dto.getClientLegalEntityDto()))
            clientLegalEntity = clientLegalEntityConverterDto.toEntity(dto.getClientLegalEntityDto());

        return Client.builder()
                .clientId(dto.getClientId())
                .typeOfClient(dto.getTypeOfClient())
                .clientManager(clientManagerService.getClientManagerById(dto.getClientManagerId())
                        .orElseThrow(() -> new NotFoundException("Client manager not found" )))
                .employee(employeeService.getEmployeeById(dto.getEmployeeId())
                        .orElseThrow(() -> new NotFoundException(("Employee not found"))))
                .clientIndividual(clientIndividual)
                .clientLegalEntity(clientLegalEntity)
                .build();
    }

    @Override
    public ClientDto toDto(Client entity) {
        ClientIndividualDto clientIndividualDto = null;
        ClientLegalEntityDto clientLegalEntityDto = null;
        if(Objects.nonNull(entity.getClientIndividual()))
            clientIndividualDto = clientIndividualConverterDto.toDto(entity.getClientIndividual());
        if(Objects.nonNull(entity.getClientLegalEntity()))
            clientLegalEntityDto = clientLegalEntityConverterDto.toDto(entity.getClientLegalEntity());

        return ClientDto.builder()
                .clientId(entity.getClientId())
                .typeOfClient(entity.getTypeOfClient())
                .clientManagerId(entity.getClientManager().getClientManagerId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .clientIndividualDto(clientIndividualDto)
                .clientLegalEntityDto(clientLegalEntityDto)
                .build();
    }
}
