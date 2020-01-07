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

import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ClientServiceTest {

    @Autowired
    RepositoryClient repositoryClient;
    @Autowired
    ClientService clientService;


    @Test
    public void getClientsFromFile() {
        File fileInd = new File("D:\\workSpace\\data\\фл.xlsx");
        File fileLExlsx = new File("D:\\workSpace\\data\\юл.xlsx");
        File fileLExls = new File("D:\\workSpace\\data\\юл.xls");

        try {
            List<Client> clientListInd = clientService.getNewClientsFromFile(fileInd, TypeOfClient.INDIVIDUAL);
            List<Client> clientListLExlsx = clientService.getNewClientsFromFile(fileLExlsx, TypeOfClient.LEGAL_ENTITY);
            List<Client> clientListLExls = clientService.getNewClientsFromFile(fileLExls, TypeOfClient.LEGAL_ENTITY);

            log.info(clientListInd.toString());
            log.info(clientListLExlsx.toString());
            log.info(clientListLExls.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}