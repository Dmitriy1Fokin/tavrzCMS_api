package ru.fds.tavrzcms3.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.domain.ClientManager;
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
public class ClientManagerServiceTest {

    @Autowired
    ClientManagerService clientManagerService;
    @Autowired
    ValidatorEntity validatorEntity;

    @Test
    public void getNewClientMAnagersFromFile() {
        try {
            List<ClientManager> clientManagerList = clientManagerService.getNewClientManagersFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\client_manager_new.xlsx"));
            clientManagerList.stream().forEach(x -> System.out.println(x));

            List<ClientManager> clientManagerListValid = new ArrayList<>();
            clientManagerList.stream().forEach(x->{
                Set<ConstraintViolation<ClientManager>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    clientManagerListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });


            assertEquals(5, clientManagerList.size());
            assertEquals(2, clientManagerListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentClientMAnagersFromFile() {
    }
}