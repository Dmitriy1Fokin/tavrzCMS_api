package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converver.dtoconverter.impl.ClientLegalEntityConverterDto;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.dto.ClientLegalEntityDto;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientLegalEntityConverterDtoTest {

    @Autowired
    ClientLegalEntityConverterDto clientLegalEntityConverter;

    @Test
    public void toEntity() {
        ClientLegalEntityDto clientLegalEntityDto = ClientLegalEntityDto.builder()
                .organizationalForm("ООО")
                .name("Имя")
                .inn("1234567890")
                .build();

        ClientLegalEntity clientLegalEntity = clientLegalEntityConverter.toEntity(clientLegalEntityDto);

        assertEquals(clientLegalEntityDto.getOrganizationalForm(), clientLegalEntity.getOrganizationalForm());
        assertEquals(clientLegalEntityDto.getName(), clientLegalEntity.getName());
        assertEquals(clientLegalEntityDto.getInn(), clientLegalEntity.getInn());

    }

    @Test
    public void toDto() {
        ClientLegalEntity clientLegalEntity = ClientLegalEntity.builder()
                .organizationalForm("ООО")
                .name("Имя")
                .inn("1234567890")
                .build();

        ClientLegalEntityDto clientLegalEntityDto = clientLegalEntityConverter.toDto(clientLegalEntity);

        assertEquals(clientLegalEntityDto.getOrganizationalForm(), clientLegalEntity.getOrganizationalForm());
        assertEquals(clientLegalEntityDto.getName(), clientLegalEntity.getName());
        assertEquals(clientLegalEntityDto.getInn(), clientLegalEntity.getInn());


    }
}