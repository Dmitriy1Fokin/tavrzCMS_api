package ru.fds.tavrzcms3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.ClientLegalEntityConverterDto;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.ClientConverterDto;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.LoanAgreementConverterDto;
import ru.fds.tavrzcms3.repository.RepositoryAppRole;
import ru.fds.tavrzcms3.repository.RepositoryAppUser;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;
import ru.fds.tavrzcms3.repository.RepositoryLoanAgreement;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tavrzcms3ApplicationTests {

    @Autowired
    RepositoryClient repositoryClient;
    @Autowired
    ClientLegalEntityConverterDto clientLegalEntityConverter;
    @Autowired
    ClientConverterDto clientConverter;
    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;
    @Autowired
    LoanAgreementConverterDto loanAgreementConverter;
    @Autowired
    RepositoryEmployee repositoryEmployee;
    @Autowired
    RepositoryAppRole repositoryAppRole;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RepositoryAppUser repositoryAppUser;


    @Test
    public void contextLoads() {
    }
}
