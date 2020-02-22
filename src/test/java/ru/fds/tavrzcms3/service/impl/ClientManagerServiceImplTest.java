package ru.fds.tavrzcms3.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.TestUtils;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.repository.RepositoryClientManager;
import ru.fds.tavrzcms3.service.ClientManagerService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientManagerServiceImplTest {

    @Autowired
    ClientManagerService clientManagerService;
    @MockBean
    RepositoryClientManager repositoryClientManager;
    @MockBean
    ValidatorEntity validatorEntity;

    @Test
    public void getAllClientManager() {
        Mockito.when(repositoryClientManager.findAll(Sort.by(Sort.Direction.ASC, "surname")))
                .thenReturn(TestUtils.getClientManagerList());
        List<ClientManager> clientManagers = clientManagerService.getAllClientManager();
        assertEquals(2, clientManagers.size());
    }

    @Test
    public void getClientManagerById() {
        Mockito.when(repositoryClientManager.findById(TestUtils.getClientManager().getClientManagerId()))
                .thenReturn(Optional.of(TestUtils.getClientManager()));
        Optional<ClientManager> clientManager = clientManagerService.getClientManagerById(TestUtils.getClientManager().getClientManagerId());
        assertTrue(clientManager.isPresent());
    }

    @Test
    public void getClientManagerByClient() {
        Mockito.when(repositoryClientManager.findByClient(1L))
                .thenReturn(Optional.of(TestUtils.getClientManager()));
        Optional<ClientManager> clientManager = clientManagerService.getClientManagerByClient(TestUtils.getClientManager().getClientManagerId());
        assertTrue(clientManager.isPresent());
    }
}