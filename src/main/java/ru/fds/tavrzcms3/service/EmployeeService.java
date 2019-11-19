package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.repository.*;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    @Autowired
    RepositoryEmployee repositoryEmployee;

    @Autowired
    RepositoryAppUser repositoryAppUser;

    public Employee getEmployeeByUser(User user){
        AppUser appUser = repositoryAppUser.findByName(user.getUsername());
        return repositoryEmployee.findByAppUser(appUser);
    }

    public Optional<Employee> getEmployeeById(long employeeId){
        return repositoryEmployee.findById(employeeId);
    }

    public List<Employee> getAllEmployee(){
        Sort sortByDateSurname = new Sort(Sort.Direction.ASC, "surname");
        return repositoryEmployee.findAll(sortByDateSurname);
    }
}
