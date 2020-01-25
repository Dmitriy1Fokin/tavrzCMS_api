package ru.fds.tavrzcms3.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.fds.tavrzcms3.configuration.AuthenticationEntryPoint;
import ru.fds.tavrzcms3.configuration.SecurityConfiguration;
import ru.fds.tavrzcms3.configuration.SecurityConfigurationDev;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
@EnableAutoConfiguration(exclude = {AuthenticationEntryPoint.class, SecurityConfiguration.class, SecurityConfigurationDev.class})
public class ClientControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ClientService clientService;
    @MockBean
    private FilesService filesService;
    @MockBean
    private ValidatorEntity validatorEntity;
    @MockBean
    private DtoFactory dtoFactory;


    @Test
    public void getAllClients() throws Exception {
        Client client = Client.builder().
                clientId(1L).
                typeOfClient(TypeOfClient.INDIVIDUAL).
                build();

        List<Client> clientList = Arrays.asList(client);

        given(clientService.getAllClients()).willReturn(clientList);

        mvc.perform(get("/client").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientId", is(client.getClientId())));
    }

    @Test
    public void getClient() {
    }

    @Test
    public void getClientBySearchCriteria() {
    }

    @Test
    public void insertClient() {
    }

    @Test
    public void updateClient() {
    }

    @Test
    public void insertClientLegalEntityFromFile() {
    }

    @Test
    public void insertClientIndividualFromFile() {
    }

    @Test
    public void updateClientLegalEntityFromFile() {
    }

    @Test
    public void updateClientIndividualFromFile() {
    }
}