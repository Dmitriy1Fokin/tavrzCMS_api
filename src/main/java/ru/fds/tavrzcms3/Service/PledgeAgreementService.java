package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;

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

    @Autowired
    RepositoryEmployee repositoryEmployee;

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

    public synchronized Set<Date> getDatesOfConclusion(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<Date> dates = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            dates.add(ps.getDateConclusion());

        return  dates;
    }

    public synchronized Set<Date> getDatesOfMonitoring(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<Date> dates = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            dates.add(ps.getDateMonitoring());

        return  dates;
    }

    public synchronized Set<String> getResultsOfMonitoring(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<String> results = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            results.add(ps.getStatusMonitoring());

        return  results;
    }

    public synchronized PledgeAgreement getPledgeAgreement(long pledgeAgreementId){
        return repositoryPledgeAgreement.getOne(pledgeAgreementId);
    }

    public synchronized Set<String> getTypeOfCollateral(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<String> typeOfCollateralSet = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            typeOfCollateralSet.add(ps.getTypeOfCollateral());

        return  typeOfCollateralSet;
    }

    public synchronized int countOfCurrentLoanAgreementsForPladgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
    }

    public synchronized List<LoanAgreement> getCurrentLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
    }

    public synchronized int countOfClosedLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
    }

    public synchronized List<LoanAgreement> getClosedLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLEEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
    }

    public synchronized List<PledgeAgreement> getCurrentPledgeAgreementsForPledgor(long pledgorId, String pervPosl){
        return repositoryPledgeAgreement.findByPledgorAndPervPoslEqualsAndStatusPEEquals(repositoryClient.getOne(pledgorId), pervPosl, "открыт");
    }

    public synchronized int countOfCurrentPledgeAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        return repositoryPledgeAgreement.countAllByPledgorInAndStatusPEEquals(pledgors, "открыт");
    }

    public synchronized int countOfPervCurrentPledgeAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        return repositoryPledgeAgreement.countAllByPledgorInAndPervPoslEqualsAndStatusPEEquals(pledgors, "перв", "открыт");
    }

    public synchronized int countOfPoslCurrentPledgeAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        return repositoryPledgeAgreement.countAllByPledgorInAndPervPoslEqualsAndStatusPEEquals(pledgors, "посл", "открыт");
    }

    public synchronized int countOfLoanAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> loaners = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.countAllByLoanerInAndStatusLEEquals(loaners, "открыт");
    }

}
