package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryAppUser;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;
import ru.fds.tavrzcms3.repository.RepositoryPledgeEgreement;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    RepositoryEmployee repositoryEmployee;

    @Autowired
    RepositoryAppUser repositoryAppUser;

    @Autowired
    RepositoryClient repositoryClient;

    @Autowired
    RepositoryPledgeEgreement repositoryPledgeEgreement;

    public synchronized Employee getEmployeeByAppUser(User user){
        AppUser appUser = repositoryAppUser.findByName(user.getUsername());
        return repositoryEmployee.findByAppUser(appUser);
    }

    public synchronized int getCountOfAllPledgeEgreements(User user){
        List<Client> clients = repositoryClient.findByEmployee(getEmployeeByAppUser(user));
        return repositoryPledgeEgreement.countAllByPledgorIn(clients);
    }

    public synchronized int getCountOfPervPledgeEgreements(User user){
        List<Client> clients = repositoryClient.findByEmployee(getEmployeeByAppUser(user));
        return repositoryPledgeEgreement.countAllByPledgorInAndPervPoslEquals(clients, "перв");
    }

    public synchronized int getCountOfPoslPledgeEgreements(User user){
        List<Client> clients = repositoryClient.findByEmployee(getEmployeeByAppUser(user));
        return repositoryPledgeEgreement.countAllByPledgorInAndPervPoslEquals(clients, "посл");
    }

    public synchronized List<PledgeAgreement> getPledgeEgreementByEmployeeId(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> clients = repositoryClient.findByEmployee(employee);
        return repositoryPledgeEgreement.findByPledgorIn(clients);
    }

    public synchronized Employee getEmployeeById(long id){
        Employee employee = repositoryEmployee.getOne(id);
        return employee;
    }
}
