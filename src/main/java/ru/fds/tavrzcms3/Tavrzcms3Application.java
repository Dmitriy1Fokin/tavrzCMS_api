package ru.fds.tavrzcms3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@SpringBootApplication
public class Tavrzcms3Application {

    public static void main(String[] args) {
        SpringApplication.run(Tavrzcms3Application.class);
    }

    @Autowired
    private RepositoryEmployee repositoryEmployee;

    @Autowired
    private RepositoryClientIndividual repositoryClientIndividual;

    @Autowired
    private RepositoryClient repositoryClient;

    @Autowired
    private RepositoryPledgeEgreement repositoryPledgeEgreement;

    @PostConstruct
    public void init(){
        ClientIndividual clientList = repositoryClientIndividual.findByPasportNum("5532 123765");
        System.out.println(clientList);

        System.out.println("-------------FIND ALL EMPLOYEES------------");
        List<Employee> employees = repositoryEmployee.findAll();
        for(Employee emp : employees)
            System.out.println(emp);

        System.out.println("-------------FIND ALL PledgeEgreement------------");
        System.out.println(employees.get(3));
        List<Client> clients = repositoryClient.findByEmployee(employees.get(3));
        for(Client c : clients)
            System.out.println(c);

        List<PledgeEgreement> pledgeEgreements = repositoryPledgeEgreement.findByPledgorIn(clients);
        for(PledgeEgreement pe : pledgeEgreements)
            System.out.println(pe);

    }
}
