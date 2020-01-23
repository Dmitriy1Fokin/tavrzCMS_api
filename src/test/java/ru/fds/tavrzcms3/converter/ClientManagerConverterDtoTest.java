package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.ClientManagerConverterDto;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.dto.ClientManagerDto;


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
                .fullName("QWE ASD ZXC")
                .build();

        ClientManager clientManager = clientManagerConverter.toEntity(clientManagerDto);

        assertEquals(clientManagerDto.getClientManagerId(), clientManager.getClientManagerId());
        assertEquals(clientManagerDto.getSurname(), clientManager.getSurname());
        assertEquals(clientManagerDto.getName(), clientManager.getName());
        assertEquals(clientManagerDto.getPatronymic(), clientManager.getPatronymic());
    }

    @Test
    public void toDto() {
        ClientManager clientManager = ClientManager.builder()
                .clientManagerId(1L)
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .build();

        ClientManagerDto clientManagerDto = clientManagerConverter.toDto(clientManager);

        assertEquals(clientManagerDto.getClientManagerId(), clientManager.getClientManagerId());
        assertEquals(clientManagerDto.getSurname(), clientManager.getSurname());
        assertEquals(clientManagerDto.getName(), clientManager.getName());
        assertEquals(clientManagerDto.getPatronymic(), clientManager.getPatronymic());
        assertEquals("QWE ASD ZXC", clientManagerDto.getFullName());
    }
}