package ru.fds.tavrzcms3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ClientServiceTest {

    @Autowired
    RepositoryClient repositoryClient;
    @Autowired
    ClientService clientService;
    @Autowired
    ValidatorEntity validatorEntity;


    @Test
    public void getNewClientsFromFile() {
        try {
            List<Client> clientIndList = clientService.getNewClientsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\client_ind_new.xlsx"), TypeOfClient.INDIVIDUAL);
            System.out.println("CLIENT INDIVIDUAL FROM FILE:");
            clientIndList.forEach(x->System.out.println(x));

            List<Client> clientLegalList = clientService.getNewClientsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\client_legal_new.xlsx"), TypeOfClient.LEGAL_ENTITY);
            System.out.println("CLIENT LEGAL ENTITY FROM FILE:");
            clientLegalList.forEach(x->System.out.println(x));

            List<Client> clientIndListValid = new ArrayList<>();
            System.out.println("CLIENT LEGAL INDIVIDUAL VALIDATION ERRORS:");
            clientIndList.forEach(x->{
                Set<ConstraintViolation<Client>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    clientIndListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<Client> clientLegalListValid = new ArrayList<>();
            System.out.println("CLIENT LEGAL LEGAL ENTITY VALIDATION ERRORS:");
            clientLegalList.forEach(x->{
                Set<ConstraintViolation<Client>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    clientLegalListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("CLIENT LEGAL INDIVIDUAL VALID:");
            clientIndListValid.forEach(x-> System.out.println(x));

            System.out.println("CLIENT LEGAL LEGAL ENTITY VALID:");
            clientLegalListValid.forEach(x-> System.out.println(x));

            assertEquals(4, clientIndList.size());
            assertEquals(2, clientIndListValid.size());
            assertEquals(4, clientLegalList.size());
            assertEquals(2, clientLegalListValid.size());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentClientsFromFile() {
        try {
            List<Client> clientIndList = clientService.getCurrentClientsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\client_ind_update.xlsx"));
            System.out.println("CLIENT INDIVIDUAL FROM FILE:");
            clientIndList.forEach(x->System.out.println(x));

            List<Client> clientLegalList = clientService.getCurrentClientsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\client_legal_update.xlsx"));
            System.out.println("CLIENT LEGAL ENTITY FROM FILE:");
            clientLegalList.forEach(x->System.out.println(x));

            List<Client> clientIndListValid = new ArrayList<>();
            System.out.println("CLIENT LEGAL INDIVIDUAL VALIDATION ERRORS:");
            clientIndList.forEach(x->{
                Set<ConstraintViolation<Client>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    clientIndListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<Client> clientLegalListValid = new ArrayList<>();
            System.out.println("CLIENT LEGAL LEGAL ENTITY VALIDATION ERRORS:");
            clientLegalList.forEach(x->{
                Set<ConstraintViolation<Client>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    clientLegalListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("CLIENT LEGAL INDIVIDUAL VALID:");
            clientIndListValid.forEach(x-> System.out.println(x));

            System.out.println("CLIENT LEGAL LEGAL ENTITY VALID:");
            clientLegalListValid.forEach(x-> System.out.println(x));

            assertEquals(4, clientIndList.size());
            assertEquals(2, clientIndListValid.size());
            assertEquals(4, clientLegalList.size());
            assertEquals(2, clientLegalListValid.size());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}