package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryPledgeEgreement;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    RepositoryClient repositoryClient;

    @Autowired
    RepositoryPledgeEgreement repositoryPledgeEgreement;

    public synchronized Client getPledgorByPledgeSubject(PledgeSubject pledgeSubject){
        PledgeAgreement pledgeAgreement = repositoryPledgeEgreement.findByPledgeSubjects(pledgeSubject);
        System.out.println(pledgeAgreement);
        List<Client> clients = repositoryClient.findByPledgeEgreements(pledgeAgreement);
//        Client client = repositoryClient.findByPledgeEgreements(pledgeAgreement);
        return null;
    }
}
