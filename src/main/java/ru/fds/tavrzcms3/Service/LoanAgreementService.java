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
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreementsAndStatusPEEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                                                    "открыт");
        return pledgeAgreementList.size();
    }

    public synchronized List<PledgeAgreement> getCurrentPledgeAgreements(long loanAgreementId){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPEEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                 "открыт");
    }

    public synchronized int countOfClosedPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreementsAndStatusPEEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                                                      "закрыт");

        return pledgeAgreementList.size();
    }

    public synchronized List<PledgeAgreement> getClosedPledgeAgreements(long loanAgreementId){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPEEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                  "закрыт");
    }

}
