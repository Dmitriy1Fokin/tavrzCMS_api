package ru.fds.tavrzcms3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientIndividual;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.repository.RepositoryClientIndividual;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;

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

    @PostConstruct
    public void init(){
        ClientIndividual clientList = repositoryClientIndividual.findByPasportNum("5532 123765");
        System.out.println(clientList);

        System.out.println("-------------FIND ALL EMPLOYEES------------");
        List<Employee> employees = repositoryEmployee.findAll();
        for(Employee emp : employees)
            System.out.println(emp);
    }
}
