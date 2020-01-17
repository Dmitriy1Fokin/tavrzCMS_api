package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.service.EmployeeService;

import javax.validation.Valid;

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

    @GetMapping("/{employeeId}")
    public EmployeeDto getEmployee(@PathVariable("employeeId") Long employeeId){
        return employeeService.getEmployeeById(employeeId).map(dtoFactory::getEmployeeDto)
                .orElseThrow(()-> new NullPointerException("Employee not found"));
    }

    @PostMapping("/insert")
    public EmployeeDto insertEmployee(@Valid @RequestBody EmployeeDto employeeDto){
        Employee employee = employeeService.updateInsertEmployee(dtoFactory.getEmployeeEntity(employeeDto));

        return dtoFactory.getEmployeeDto(employee);
    }

    @PutMapping("/update")
    public EmployeeDto updateEmployee(@Valid @RequestBody EmployeeDto employeeDto){
        return insertEmployee(employeeDto);
    }
}
