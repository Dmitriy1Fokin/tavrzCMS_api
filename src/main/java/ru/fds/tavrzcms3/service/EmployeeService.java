package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> getEmployeeById(Long employeeId);
    List<Employee> getAllEmployee();
    List<Employee> getEmployeesExcludeEmployee(Long employeeId);
    Optional<Employee> getEmployeeByLoanAgreement(Long loanAgreementId);
    Optional<Employee> getEmployeeByPledgeAgreement(Long pledgeAgreementId);
    Employee updateInsertEmployee(Employee employee);
}
