package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.service.AppUserDetailsService;
import ru.fds.tavrzcms3.service.ClientService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class EmployeeConverterDto implements ConverterDto<Employee, EmployeeDto> {

    private final AppUserDetailsService appUserDetailsService;
    private final ClientService clientService;

    public EmployeeConverterDto(AppUserDetailsService appUserDetailsService,
                                ClientService clientService) {
        this.appUserDetailsService = appUserDetailsService;
        this.clientService = clientService;
    }

    @Override
    public Employee toEntity(EmployeeDto dto) {
        Optional<AppUser> appUser = Optional.empty();
        if(Objects.nonNull(dto.getEmployeeId())){
            appUser = appUserDetailsService.getAppUserByEmployeeId(dto.getEmployeeId());
        }

        List<Long> clientsIds = Collections.emptyList();
        if(Objects.nonNull(dto.getClientsIds())){
            clientsIds = dto.getClientsIds();
        }

        return Employee.builder()
                .employeeId(dto.getEmployeeId())
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .appUser(appUser.orElse(null))
                .clients(clientService.getClientsByIds(clientsIds))
                .build();
    }

    @Override
    public EmployeeDto toDto(Employee entity) {
        List<Long> clientsIdsList = new ArrayList<>();
        for(Client c : clientService.getClientByEmployee(entity))
            clientsIdsList.add(c.getClientId());

        String fullName = getFullName(entity.getSurname(), entity.getName(), entity.getPatronymic());

        Long appUserId = null;
        if(Objects.nonNull(entity.getAppUser()))
            appUserId = entity.getAppUser().getUserId();

        return EmployeeDto.builder()
                .employeeId(entity.getEmployeeId())
                .surname(entity.getSurname())
                .name(entity.getName())
                .patronymic(entity.getPatronymic())
                .appUserId(appUserId)
                .clientsIds(clientsIdsList)
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
