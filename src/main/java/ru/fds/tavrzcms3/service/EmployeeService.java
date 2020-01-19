package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    private final RepositoryEmployee repositoryEmployee;

    public EmployeeService(RepositoryEmployee repositoryEmployee) {
        this.repositoryEmployee = repositoryEmployee;
    }

    public Optional<Employee> getEmployeeById(long employeeId){
        return repositoryEmployee.findById(employeeId);
    }

    public List<Employee> getAllEmployee(){
        Sort sortByDateSurname = new Sort(Sort.Direction.ASC, "surname");
        return repositoryEmployee.findAll(sortByDateSurname);
    }

    public List<Employee> getEmployeesExcludeEmployee(Long employeeId){
        return repositoryEmployee.findAllByEmployeeIdNot(employeeId);
    }

    public Employee getEmployeeByLoanAgreement(long loanAgreementId){
        return repositoryEmployee.getEmployeeByLoanAgreement(loanAgreementId);
    }

    public Employee getEmployeeByPledgeAgreement(long pledgeAgreementId){
        return repositoryEmployee.getEmployeeByPledgeAgreement(pledgeAgreementId);
    }

    @Transactional
    public Employee updateInsertEmployee(Employee employee){
        return repositoryEmployee.save(employee);
    }
}
