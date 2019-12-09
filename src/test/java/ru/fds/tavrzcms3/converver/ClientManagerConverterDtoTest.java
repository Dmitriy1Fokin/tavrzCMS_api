package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.dto.ClientManagerDto;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientManagerConverterDtoTest {

    @Autowired
    ClientManagerConverterDto clientManagerConverter;

    @Test
    public void toEntity() {
        ClientManagerDto clientManagerDto = ClientManagerDto.builder()
                .clientManagerId(1L)
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .clientsIds(Arrays.asList(6L, 7L))
                .fullName("QWE ASD ZXC")
                .build();

        ClientManager clientManager = clientManagerConverter.toEntity(clientManagerDto);

        assertEquals(clientManagerDto.getClientManagerId(), clientManager.getClientManagerId());
        assertEquals(clientManagerDto.getSurname(), clientManager.getSurname());
        assertEquals(clientManagerDto.getName(), clientManager.getName());
        assertEquals(clientManagerDto.getPatronymic(), clientManager.getPatronymic());
        assertEquals(clientManagerDto.getClientsIds().size(), clientManager.getClients().size());
    }

    @Test
    public void toDto() {
        ClientManager clientManager = ClientManager.builder()
                .clientManagerId(1L)
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .client(new Client().builder().clientId(3L).build())
                .build();

        ClientManagerDto clientManagerDto = clientManagerConverter.toDto(clientManager);

        assertEquals(clientManagerDto.getClientManagerId(), clientManager.getClientManagerId());
        assertEquals(clientManagerDto.getSurname(), clientManager.getSurname());
        assertEquals(clientManagerDto.getName(), clientManager.getName());
        assertEquals(clientManagerDto.getPatronymic(), clientManager.getPatronymic());
        assertEquals("QWE ASD ZXC", clientManagerDto.getFullName());
    }
}