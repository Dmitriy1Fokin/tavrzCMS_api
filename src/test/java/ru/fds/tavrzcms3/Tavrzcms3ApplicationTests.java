package ru.fds.tavrzcms3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.converver.ClientLegalEntityConverterDto;
import ru.fds.tavrzcms3.converver.ClientConverterDto;
import ru.fds.tavrzcms3.converver.LoanAgreementConverterDto;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryClientLegalEntity;
import ru.fds.tavrzcms3.repository.RepositoryLoanAgreement;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tavrzcms3ApplicationTests {

    @Autowired
    RepositoryClient repositoryClient;
    @Autowired
    RepositoryClientLegalEntity repositoryClientLegalEntity;
    @Autowired
    ClientLegalEntityConverterDto clientLegalEntityConverter;
    @Autowired
    ClientConverterDto clientConverter;
    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;
    @Autowired
    LoanAgreementConverterDto loanAgreementConverter;


    @Test
    public void contextLoads() {
    }

    @Test
    public void testTypeOfAuto(){
        TypeOfAuto[] typeOfAuto = TypeOfAuto.values();
        for (TypeOfAuto t : typeOfAuto)
            System.out.println(t.ordinal() + ", " + t.toString() + ", " + t.getTranslate());
    }

    @Test
    public void testTypeOfSecurities(){
        TypeOfSecurities[] typeOfSecurities = TypeOfSecurities.values();
        for (TypeOfSecurities t : typeOfSecurities){
            System.out.println(t.ordinal() + ", " + t.name() + ", " + t.getTranslate());
        }
    }

    @Test
    public void testTypeOfCollateral(){
        TypeOfCollateral[] typeOfCollaterals = TypeOfCollateral.values();
        for(TypeOfCollateral t : typeOfCollaterals)
            System.out.println(t.ordinal() + ", " + t.name() + ", " + t.getTranslate());
    }

    @Test
    public void testClientDto(){
        Client client = repositoryClient.findById(246L).get();
        System.out.println(client);

        ClientDto clientDto = clientConverter.toDto(client);
        System.out.println(clientDto);

        Client client1 = clientConverter.toEntity(clientDto);
        System.out.println(client1);

    }

}
