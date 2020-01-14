package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.service.EmployeeService;

import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DtoFactory dtoFactory;

    public EmployeeController(EmployeeService employeeService,
                              DtoFactory dtoFactory) {
        this.employeeService = employeeService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id){
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(dtoFactory::getEmployeeDto).orElse(null);
    }
}
