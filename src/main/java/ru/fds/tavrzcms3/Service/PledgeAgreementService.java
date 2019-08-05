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
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.*;

@Service
public class PledgeAgreementService {

    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;

    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;

    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;

    @Autowired
    RepositoryClient repositoryClient;

    public synchronized double getRsDz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for (PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsDz();

        return  totalSum;
    }

    public synchronized double getRsZz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsZz();

        return totalSum;
    }

    public synchronized double getZsDz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsDz();

        return totalSum;
    }

    public synchronized double getZsZz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsZz();

        return totalSum;
    }

    public synchronized double getSs(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getSs();

        return totalSum;
    }

    public synchronized List<Date> getDatesOfConclusion(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<Date> dates = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects){
            Date date = ps.getDateConclusion();
            if(!dates.contains(date))
                dates.add(date);
        }

        return  dates;
    }

    public synchronized List<Date> getDatesOfMonitoring(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<Date> dates = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects){
            Date date = ps.getDateMonitoring();
            if(!dates.contains(date))
                dates.add(date);
        }

        return  dates;
    }

    public synchronized List<String> getResultsOfMonitoring(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<String> results = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects){
            String resultOfMonitoring = ps.getStatusMonitoring();
            if(!results.contains(resultOfMonitoring))
                results.add(resultOfMonitoring);
        }

        return  results;
    }

    public synchronized PledgeAgreement getPledgeAgreementById(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        return pledgeAgreement;
    }

    public synchronized Set<String> getTypeOfCollateral(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<String> typeOfCollateralSet = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            typeOfCollateralSet.add(ps.getTypeOfCollateral());

        return  typeOfCollateralSet;
    }

    public synchronized int countOfCurrentLoanAgreements(long pledgeAgreementId){
        List<LoanAgreement> loanAgreementList =
                repositoryLoanAgreement.findByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
        return loanAgreementList.size();
    }

    public synchronized List<LoanAgreement> getCurrentLoanAgreements(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
    }

    public synchronized int countOfClosedLoanAgreements(long pledgeAgreementId){
        List<LoanAgreement> loanAgreementList =
                repositoryLoanAgreement.findByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
        return loanAgreementList.size();
    }

    public synchronized List<LoanAgreement> getClosedLoanAgreements(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
    }

    public synchronized List<PledgeAgreement> getPledgeEgreementsByPledgorIdAndPervPosl(long pledgorId, String pervPosl){
        return repositoryPledgeAgreement.findByPledgorAndPervPoslEquals(repositoryClient.getOne(pledgorId), pervPosl);
    }

}
