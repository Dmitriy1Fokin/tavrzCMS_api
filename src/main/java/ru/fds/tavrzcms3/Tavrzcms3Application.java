package ru.fds.tavrzcms3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.fds.tavrzcms3.domain.ClientIndividual;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.repository.ClientIndividualRepository;
import ru.fds.tavrzcms3.repository.ClientManagerRepository;
import ru.fds.tavrzcms3.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Tavrzcms3Application {

    public static void main(String[] args) {

//        SpringApplication.run(Tavrzcms3Application.class, args);
        ApplicationContext context = SpringApplication.run(Tavrzcms3Application.class);

//        EmployeeRepository repository = context.getBean(EmployeeRepository.class);
//        Employee employee = new Employee();
//        employee.setSurname("qwert");
//        employee.setName("Ниefghна");
//        employee.setPatronymic("Влаdfghдимировна");
//        System.out.println("After: = " + employee.toString());
//        repository.insert(employee);
//        System.out.println("Before: = " + employee.toString());
//        employee.setPatronymic("Николавевна");
//        repository.update(employee);
//        System.out.println("Before2: = " + employee.toString());

//        Employee employee = repository.getById(3);
//        System.out.println("Emp 1 = " + employee.toString());
//        employee.setPatronymic("1werf");
//        repository.update(employee);
//        System.out.println("Emp 1 = " + employee.toString());

//        Employee employee = repository.getById(3);
//        repository.delete(employee);
//        List<Employee> employeeList = new ArrayList<>();
//        employeeList = repository.getAll();
//        for(Employee e : employeeList)
//            System.out.println(e.toString());

//        ClientManager clientManager = new ClientManager();
//        clientManager.setSurname("Романенко");
//        clientManager.setName("Ктрилл");
//        clientManager.setPatronymic("Юрьевич");
//        ClientManagerRepository clientManagerRepository = context.getBean(ClientManagerRepository.class);
//        System.out.println("ClientManager before insert = " + clientManager.toString());
//        clientManagerRepository.insert(clientManager);
//        System.out.println("ClientManager after insert = " + clientManager.toString());


//        ClientIndividual clientIndividual = new ClientIndividual();
//        clientIndividual.setSurname("Иванов");
//        clientIndividual.setName("Иван");
//        clientIndividual.setPatronymic("Иванович");
//        clientIndividual.setPasportNum("5532 123765");
//        ClientManagerRepository clientManagerRepository = context.getBean(ClientManagerRepository.class);
//        clientIndividual.setClientManager(clientManagerRepository.getBySurname("Романенко"));
//        EmployeeRepository employeeRepository = context.getBean(EmployeeRepository.class);
//        clientIndividual.setEmployee(employeeRepository.getBySurname("Фокин"));
//        ClientIndividualRepository clientIndividualRepository = context.getBean(ClientIndividualRepository.class);
//        System.out.println("ClientIndividual before insert = " + clientIndividual.toString());
//        clientIndividualRepository.insert(clientIndividual);
//        System.out.println("ClientIndividual after insert = " + clientIndividual.toString());

        ClientIndividualRepository clientIndividualRepository = context.getBean(ClientIndividualRepository.class);
        ClientIndividual clientIndividual = clientIndividualRepository.getById(2);
        System.out.println(clientIndividual);

    }

}
