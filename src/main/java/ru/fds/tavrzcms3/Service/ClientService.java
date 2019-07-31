package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.PledgeEgreement;
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
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.findByPledgeSubjects(pledgeSubject);
        System.out.println(pledgeEgreement);
        List<Client> clients = repositoryClient.findByPledgeEgreements(pledgeEgreement);
//        Client client = repositoryClient.findByPledgeEgreements(pledgeEgreement);
        return null;
    }
}
