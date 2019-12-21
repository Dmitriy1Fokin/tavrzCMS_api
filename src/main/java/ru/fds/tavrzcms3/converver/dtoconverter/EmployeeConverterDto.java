package ru.fds.tavrzcms3.converver.dtoconverter;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.service.AppUserDetailsService;
import ru.fds.tavrzcms3.service.ClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return Employee.builder()
                .employeeId(dto.getEmployeeId())
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .appUser(appUserDetailsService.getAppUserByEmployeeId(dto.getEmployeeId()).orElse(null))
                .clients(clientService.getClientsByIds(dto.getClientsIds()))
                .build();
    }

    @Override
    public EmployeeDto toDto(Employee entity) {
        List<Long> clientsIdsList = new ArrayList<>();
        for(Client c : clientService.getClientByEmployee(entity))
            clientsIdsList.add(c.getClientId());

        StringBuilder fullName = new StringBuilder();
        if(Objects.nonNull(entity.getSurname()) && !entity.getSurname().isEmpty())
            fullName.append(entity.getSurname());
        if(Objects.nonNull(entity.getName()) && !entity.getName().isEmpty())
            fullName.append(" ").append(entity.getName());
        if(Objects.nonNull(entity.getPatronymic()) && !entity.getPatronymic().isEmpty())
            fullName.append(" ").append(entity.getPatronymic());

        return EmployeeDto.builder()
                .employeeId(entity.getEmployeeId())
                .surname(entity.getSurname())
                .name(entity.getName())
                .patronymic(entity.getPatronymic())
                .appUserId(entity.getAppUser().getUserId())
                .clientsIds(clientsIdsList)
                .fullName(fullName.toString())
                .build();
    }
}
