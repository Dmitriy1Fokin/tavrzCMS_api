package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.ClientIndividualDto;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientIndividualConverterDtoTest {

    @Autowired
    ClientIndividualConverterDto clientIndividualConverter;

    @Test
    public void toEntity() {
        ClientIndividualDto clientIndividualDto = ClientIndividualDto.builder()
                .clientId(1L)
                .typeOfClient(TypeOfClient.INDIVIDUAL)
                .clientManagerId(1L)
                .employeeId(1L)
                .loanAgreementsIds(Arrays.asList(4L, 5L))
                .pledgeAgreementsIds(Arrays.asList(6L, 7L))
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .pasportNum("1234 54321ё")
                .fullName("QWE ASD ZXC")
                .build();

        ClientIndividual clientIndividual = clientIndividualConverter.toEntity(clientIndividualDto);

        assertEquals(clientIndividualDto.getClientId(), clientIndividual.getClientId());
        assertEquals(clientIndividualDto.getTypeOfClient(), clientIndividual.getTypeOfClient());
        assertEquals(clientIndividualDto.getClientManagerId(), clientIndividual.getClientManager().getClientManagerId());
        assertEquals(clientIndividualDto.getEmployeeId(), clientIndividual.getEmployee().getEmployeeId());
        assertEquals(clientIndividualDto.getLoanAgreementsIds().size(), clientIndividual.getLoanAgreements().size());
        assertEquals(clientIndividualDto.getPledgeAgreementsIds().size(), clientIndividual.getPledgeAgreements().size());
        assertEquals(clientIndividualDto.getSurname(), clientIndividual.getSurname());
        assertEquals(clientIndividualDto.getName(), clientIndividual.getName());
        assertEquals(clientIndividualDto.getPatronymic(), clientIndividual.getPatronymic());
        assertEquals(clientIndividualDto.getPasportNum(), clientIndividual.getPasportNum());

    }

    @Test
    public void toDto() {
        ClientIndividual clientIndividual = ClientIndividual.builder()
                .clientId(1L)
                .typeOfClient(TypeOfClient.LEGAL_ENTITY)
                .clientManager(new ClientManager().builder().clientManagerId(3L).build())
                .employee(new Employee().builder().employeeId(4L).build())
                .loanAgreement(new LoanAgreement().builder().loanAgreementId(4L).build())
                .pledgeAgreement(new PledgeAgreement().builder().pledgeAgreementId(7L).build())
                .surname("QWE")
                .name("ASD")
                .patronymic("ZXC")
                .pasportNum("1234 54321ё")
                .build();

        ClientIndividualDto clientIndividualDto = clientIndividualConverter.toDto(clientIndividual);

        assertEquals(clientIndividual.getClientId(), clientIndividualDto.getClientId());
        assertEquals(clientIndividual.getTypeOfClient(), clientIndividualDto.getTypeOfClient());
        assertEquals(clientIndividual.getClientManager().getClientManagerId(), clientIndividualDto.getClientManagerId());
        assertEquals(clientIndividual.getEmployee().getEmployeeId(), clientIndividualDto.getEmployeeId());
        assertEquals(clientIndividual.getLoanAgreements().size(), clientIndividualDto.getLoanAgreementsIds().size());
        assertEquals(clientIndividual.getPledgeAgreements().size(), clientIndividualDto.getPledgeAgreementsIds().size());
        assertEquals(clientIndividual.getSurname(), clientIndividualDto.getSurname());
        assertEquals(clientIndividual.getName(), clientIndividualDto.getName());
        assertEquals(clientIndividual.getPatronymic(), clientIndividualDto.getPatronymic());
        assertEquals(clientIndividual.getPasportNum(), clientIndividualDto.getPasportNum());
        assertEquals("QWE ASD ZXC", clientIndividualDto.getFullName());

    }
}