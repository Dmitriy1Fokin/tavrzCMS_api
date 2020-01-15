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
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ValidatorEntity validatorEntity;
    private final DtoFactory dtoFactory;

    public EmployeeController(EmployeeService employeeService,
                              ValidatorEntity validatorEntity,
                              DtoFactory dtoFactory) {
        this.employeeService = employeeService;
        this.validatorEntity = validatorEntity;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id){
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(dtoFactory::getEmployeeDto)
                .orElseThrow(()-> new NullPointerException("Employee not found"));
    }

    @PostMapping("/insert")
    public EmployeeDto insertEmployee(@Valid @RequestBody EmployeeDto employeeDto){
        Employee employee = dtoFactory.getEmployeeEntity(employeeDto);

        Set<ConstraintViolation<Employee>> violations =  validatorEntity.validateEntity(employee);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        employee = employeeService.updateInsertEmployee(employee);
        return dtoFactory.getEmployeeDto(employee);
    }

    @PutMapping("/update")
    public EmployeeDto updateEmployee(@Valid @RequestBody EmployeeDto employeeDto){
        return insertEmployee(employeeDto);
    }
}
