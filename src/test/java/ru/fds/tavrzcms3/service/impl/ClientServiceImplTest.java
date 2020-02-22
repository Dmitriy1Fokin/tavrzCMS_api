package ru.fds.tavrzcms3.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.TestUtils;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.specification.Search;
import ru.fds.tavrzcms3.specification.impl.SpecificationImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceImplTest {

    @Autowired
    ClientService clientService;

    @MockBean
    RepositoryClient repositoryClient;




    @Test
    public void getFullNameClient() {
        Client client = TestUtils.getClient();
        Mockito.when(repositoryClient.findById(client.getClientId())).thenReturn(Optional.of(client));

        String clientName = clientService.getFullNameClient(client.getClientId());

        StringBuilder expectedName = new StringBuilder();
        expectedName.append(client.getClientLegalEntity().getOrganizationalForm())
                .append(" \"")
                .append(client.getClientLegalEntity().getName())
                .append("\"");
        assertEquals(expectedName.toString(), clientName);
    }

    @Test
    public void getClientFromSearch() throws ReflectiveOperationException {
        Search<Client> clientSearch = new Search<>(Client.class);
        Specification<Client> specification = clientSearch.getSpecification();
        Mockito.when(repositoryClient.findAll(specification)).thenReturn(TestUtils.getClientList());
        Map<String, String> searchParam = new HashMap<>();
        searchParam.put("typeOfClient", "LEGAL_ENTITY");
        searchParam.put("name", "name1");
        searchParam.put("nameOption", "EQUAL_IGNORE_CASE");

        List<Client> clients = clientService.getClientFromSearch(searchParam);

        assertEquals(0, clients.size());
    }


}