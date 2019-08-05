package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    RepositoryClient repositoryClient;

    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;



    public synchronized Client getClientByClientId(long clientId){
        return repositoryClient.getOne(clientId);
    }

    public synchronized int countOfCurrentPledgeAgreements(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPEEquals(repositoryClient.getOne(pledgorId), "открыт");
    }

    public synchronized List<PledgeAgreement> getCurrentPledgeAgreements(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPE(repositoryClient.getOne(pledgorId), "открыт");
    }

    public synchronized int countOfClosedPledgeAgreements(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPEEquals(repositoryClient.getOne(pledgorId), "закрыт");
    }

    public synchronized List<PledgeAgreement> getClosedPledgeAgreements(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPE(repositoryClient.getOne(pledgorId), "закрыт");
    }
}
