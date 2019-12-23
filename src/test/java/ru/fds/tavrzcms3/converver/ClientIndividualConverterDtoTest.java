package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converver.dtoconverter.impl.ClientIndividualConverterDto;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.dto.ClientIndividualDto;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientIndividualConverterDtoTest {

    @Autowired
    ClientIndividualConverterDto clientIndividualConverter;

    @Test
    public void toEntity() {
        ClientIndividualDto clientIndividualDto = ClientIndividualDto.builder()
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .pasportNum("1234 54321ё")
                .build();

        ClientIndividual clientIndividual = clientIndividualConverter.toEntity(clientIndividualDto);

        assertEquals(clientIndividualDto.getSurname(), clientIndividual.getSurname());
        assertEquals(clientIndividualDto.getName(), clientIndividual.getName());
        assertEquals(clientIndividualDto.getPatronymic(), clientIndividual.getPatronymic());
        assertEquals(clientIndividualDto.getPasportNum(), clientIndividual.getPasportNum());

    }

    @Test
    public void toDto() {
        ClientIndividual clientIndividual = ClientIndividual.builder()
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .pasportNum("1234 54321ё")
                .build();

        ClientIndividualDto clientIndividualDto = clientIndividualConverter.toDto(clientIndividual);

        assertEquals(clientIndividual.getSurname(), clientIndividualDto.getSurname());
        assertEquals(clientIndividual.getName(), clientIndividualDto.getName());
        assertEquals(clientIndividual.getPatronymic(), clientIndividualDto.getPatronymic());
        assertEquals(clientIndividual.getPasportNum(), clientIndividualDto.getPasportNum());

    }
}