package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryLoanAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanAgreementService {

    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;
    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;

    public synchronized LoanAgreement getLoanAgreementById(long loanAgreementId){
        LoanAgreement loanAgreement = repositoryLoanAgreement.getOne(loanAgreementId);
        return loanAgreement;
    }

    public synchronized int countOfCurrentPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreements(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId));
        int count = 0;
        for (PledgeAgreement pa : pledgeAgreementList){
            if(pa.getStatusPE().equals("открыт"))
                count += 1;
        }
        return count;
    }

    public synchronized List<PledgeAgreement> getCurrentPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreements(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId));
        List<PledgeAgreement> currentpledgeAgreementList = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(pa.getStatusPE().equals("открыт"))
                currentpledgeAgreementList.add(pa);
        return currentpledgeAgreementList;
    }

    public synchronized int countOfClosedPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreements(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId));
        int count = 0;
        for (PledgeAgreement pa : pledgeAgreementList){
            if(pa.getStatusPE().equals("закрыт"))
                count += 1;
        }
        return count;
    }

    public synchronized List<PledgeAgreement> getClosedPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreements(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId));
        List<PledgeAgreement> currentpledgeAgreementList = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(pa.getStatusPE().equals("закрыт"))
                currentpledgeAgreementList.add(pa);
        return currentpledgeAgreementList;
    }

}
