package ru.fds.tavrzcms3.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;
import ru.fds.tavrzcms3.service.EmployeeService;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final RepositoryEmployee repositoryEmployee;

    public EmployeeServiceImpl(RepositoryEmployee repositoryEmployee) {
        this.repositoryEmployee = repositoryEmployee;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long employeeId){
        return repositoryEmployee.findById(employeeId);
    }

    @Override
    public List<Employee> getAllEmployee(){
        return repositoryEmployee.findAll(Sort.by(Sort.Direction.ASC, "surname"));
    }

    @Override
    public List<Employee> getEmployeesExcludeEmployee(Long employeeId){
        return repositoryEmployee.findAllByEmployeeIdNot(employeeId);
    }

    @Override
    public Optional<Employee> getEmployeeByLoanAgreement(Long loanAgreementId){
        return repositoryEmployee.getEmployeeByLoanAgreement(loanAgreementId);
    }

    @Override
    public Optional<Employee> getEmployeeByPledgeAgreement(Long pledgeAgreementId){
        return repositoryEmployee.getEmployeeByPledgeAgreement(pledgeAgreementId);
    }

    @Override
    @Transactional
    public Employee updateInsertEmployee(Employee employee){
        return repositoryEmployee.save(employee);
    }
}
