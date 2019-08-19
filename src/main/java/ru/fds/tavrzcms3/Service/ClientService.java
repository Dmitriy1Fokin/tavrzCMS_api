package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
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



    public Client getClientByClientId(long clientId){
        return repositoryClient.getOne(clientId);
    }

    public int countOfCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPAEquals(repositoryClient.getOne(pledgorId), "открыт");
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPA(repositoryClient.getOne(pledgorId), "открыт");
    }

    public int countOfClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPAEquals(repositoryClient.getOne(pledgorId), "закрыт");
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPA(repositoryClient.getOne(pledgorId), "закрыт");
    }

    public int countOfCurrentLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "открыт");
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "открыт");
    }

    public int countOfClosedLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "закрыт");
    }

    public List<LoanAgreement> getClosedLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "закрыт");
    }
}
