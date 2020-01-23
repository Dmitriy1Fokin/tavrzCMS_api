package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.ClientConverterDto;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.ClientDto;

import static org.junit.Assert.*;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientConverterDtoTest {

    @Autowired
    ClientConverterDto clientConverter;

    @Test
    public void toEntity() {
        ClientDto clientDto = ClientDto.builder()
                .clientId(1L)
                .typeOfClient(TypeOfClient.INDIVIDUAL)
                .clientManagerId(2L)
                .employeeId(1L)
                .build();

        Client client = clientConverter.toEntity(clientDto);
        client.getClientManager();

        assertEquals(clientDto.getClientId(), client.getClientId());
        assertEquals(clientDto.getTypeOfClient(), client.getTypeOfClient());
        assertEquals(clientDto.getClientManagerId(), client.getClientManager().getClientManagerId());
        assertEquals(clientDto.getEmployeeId(), client.getEmployee().getEmployeeId());

    }

    @Test
    public void toDto() {

        Client client = Client.builder()
                .clientId(1L)
                .typeOfClient(TypeOfClient.LEGAL_ENTITY)
                .clientManager(new ClientManager().builder().clientManagerId(18L).build())
                .employee(new Employee().builder().employeeId(4L).build())
                .build();

        ClientDto clientDto = clientConverter.toDto(client);

        assertEquals(client.getClientId(), clientDto.getClientId());
        assertEquals(client.getTypeOfClient(), clientDto.getTypeOfClient());
        assertEquals(client.getClientManager().getClientManagerId(), clientDto.getClientManagerId());
        assertEquals(client.getEmployee().getEmployeeId(), clientDto.getEmployeeId());

    }
}