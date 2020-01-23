package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.service.AppUserDetailsService;

import java.util.Objects;

@Component
public class EmployeeConverterDto implements ConverterDto<Employee, EmployeeDto> {

    private final AppUserDetailsService appUserDetailsService;

    public EmployeeConverterDto(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public Employee toEntity(EmployeeDto dto) {
       AppUser appUser = null;
        if(Objects.nonNull(dto.getEmployeeId())){
            appUser = appUserDetailsService.getAppUserByEmployeeId(dto.getEmployeeId()).orElse(null);
        }
        return Employee.builder()
                .employeeId(dto.getEmployeeId())
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .appUser(appUser)
                .build();
    }

    @Override
    public EmployeeDto toDto(Employee entity) {
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
