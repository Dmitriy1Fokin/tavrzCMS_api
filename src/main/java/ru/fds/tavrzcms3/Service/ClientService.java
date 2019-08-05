package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryLoanAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    RepositoryClient repositoryClient;

    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;

    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;



    public synchronized Client getClientByClientId(long clientId){
        return repositoryClient.getOne(clientId);
    }

    public synchronized int countOfCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPEEquals(repositoryClient.getOne(pledgorId), "открыт");
    }

    public synchronized List<PledgeAgreement> getCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPE(repositoryClient.getOne(pledgorId), "открыт");
    }

    public synchronized int countOfClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPEEquals(repositoryClient.getOne(pledgorId), "закрыт");
    }

    public synchronized List<PledgeAgreement> getClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPE(repositoryClient.getOne(pledgorId), "закрыт");
    }

    public synchronized int countOfCurrentLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByLoanerAndStatusLEEquals(repositoryClient.getOne(loanerId), "открыт");
    }

    public synchronized List<LoanAgreement> getCurrentLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByLoanerAndStatusLEEquals(repositoryClient.getOne(loanerId), "открыт");
    }

    public synchronized int countOfClosedLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByLoanerAndStatusLEEquals(repositoryClient.getOne(loanerId), "закрыт");
    }

    public synchronized List<LoanAgreement> getClosedLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByLoanerAndStatusLEEquals(repositoryClient.getOne(loanerId), "закрыт");
    }
}
