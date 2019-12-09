package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.ClientLegalEntityDto;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientLegalEntityConverterDtoTest {

    @Autowired
    ClientLegalEntityConverterDto clientLegalEntityConverter;

    @Test
    public void toEntity() {
        ClientLegalEntityDto clientLegalEntityDto = ClientLegalEntityDto.builder()
                .clientId(1L)
                .typeOfClient(TypeOfClient.INDIVIDUAL)
                .clientManagerId(1L)
                .employeeId(1L)
                .loanAgreementsIds(Arrays.asList(4L, 5L))
                .pledgeAgreementsIds(Arrays.asList(6L, 7L))
                .organizationalForm("ООО")
                .name("Имя")
                .inn("1234567890")
                .fullName("ООО \"Имя\"")
                .build();

        ClientLegalEntity clientLegalEntity = clientLegalEntityConverter.toEntity(clientLegalEntityDto);

        assertEquals(clientLegalEntityDto.getClientId(), clientLegalEntity.getClientId());
        assertEquals(clientLegalEntityDto.getTypeOfClient(), clientLegalEntity.getTypeOfClient());
        assertEquals(clientLegalEntityDto.getClientManagerId(), clientLegalEntity.getClientManager().getClientManagerId());
        assertEquals(clientLegalEntityDto.getEmployeeId(), clientLegalEntity.getEmployee().getEmployeeId());
        assertEquals(clientLegalEntityDto.getLoanAgreementsIds().size(), clientLegalEntity.getLoanAgreements().size());
        assertEquals(clientLegalEntityDto.getPledgeAgreementsIds().size(), clientLegalEntity.getPledgeAgreements().size());
        assertEquals(clientLegalEntityDto.getOrganizationalForm(), clientLegalEntity.getOrganizationalForm());
        assertEquals(clientLegalEntityDto.getName(), clientLegalEntity.getName());
        assertEquals(clientLegalEntityDto.getInn(), clientLegalEntity.getInn());

    }

    @Test
    public void toDto() {
        ClientLegalEntity clientLegalEntity = ClientLegalEntity.builder()
                .clientId(1L)
                .typeOfClient(TypeOfClient.LEGAL_ENTITY)
                .clientManager(new ClientManager().builder().clientManagerId(3L).build())
                .employee(new Employee().builder().employeeId(4L).build())
                .loanAgreement(new LoanAgreement().builder().loanAgreementId(4L).build())
                .pledgeAgreement(new PledgeAgreement().builder().pledgeAgreementId(7L).build())
                .organizationalForm("ООО")
                .name("Имя")
                .inn("1234567890")
                .build();

        ClientLegalEntityDto clientLegalEntityDto = clientLegalEntityConverter.toDto(clientLegalEntity);

        assertEquals(clientLegalEntityDto.getClientId(), clientLegalEntity.getClientId());
        assertEquals(clientLegalEntityDto.getTypeOfClient(), clientLegalEntity.getTypeOfClient());
        assertEquals(clientLegalEntityDto.getClientManagerId(), clientLegalEntity.getClientManager().getClientManagerId());
        assertEquals(clientLegalEntityDto.getEmployeeId(), clientLegalEntity.getEmployee().getEmployeeId());
        assertEquals(clientLegalEntityDto.getLoanAgreementsIds().size(), clientLegalEntity.getLoanAgreements().size());
        assertEquals(clientLegalEntityDto.getPledgeAgreementsIds().size(), clientLegalEntity.getPledgeAgreements().size());
        assertEquals(clientLegalEntityDto.getOrganizationalForm(), clientLegalEntity.getOrganizationalForm());
        assertEquals(clientLegalEntityDto.getName(), clientLegalEntity.getName());
        assertEquals(clientLegalEntityDto.getInn(), clientLegalEntity.getInn());
        assertEquals("ООО \"Имя\"", clientLegalEntityDto.getFullName());


    }
}