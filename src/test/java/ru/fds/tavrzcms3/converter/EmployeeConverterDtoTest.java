package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.EmployeeConverterDto;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.dto.EmployeeDto;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeConverterDtoTest {

    @Autowired
    EmployeeConverterDto employeeConverter;

    @Test
    public void toEntity() {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .employeeId(1L)
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .appUserId(1L)
                .fullName("QWE ASD ZXC")
                .build();

        Employee employee = employeeConverter.toEntity(employeeDto);

        assertEquals(employeeDto.getEmployeeId(), employee.getEmployeeId());
        assertEquals(employeeDto.getSurname(), employee.getSurname());
        assertEquals(employeeDto.getName(), employee.getName());
        assertEquals(employeeDto.getPatronymic(), employee.getPatronymic());
        assertEquals(employeeDto.getAppUserId(), employee.getAppUser().getUserId());

    }

    @Test
    public void toDto() {
        Employee employee = Employee.builder()
                .employeeId(1L)
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .appUser(new AppUser().builder().userId(1L).build())
                .build();

        EmployeeDto employeeDto = employeeConverter.toDto(employee);

        assertEquals(employeeDto.getEmployeeId(), employee.getEmployeeId());
        assertEquals(employeeDto.getSurname(), employee.getSurname());
        assertEquals(employeeDto.getName(), employee.getName());
        assertEquals(employeeDto.getPatronymic(), employee.getPatronymic());
        assertEquals(employeeDto.getAppUserId(), employee.getAppUser().getUserId());
        assertEquals("QWE ASD ZXC", employeeDto.getFullName());

    }
}