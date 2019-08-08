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
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
    }

    public synchronized List<LoanAgreement> getCurrentLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
    }

    public synchronized int countOfClosedLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
    }

    public synchronized List<LoanAgreement> getClosedLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
    }

    public synchronized List<PledgeAgreement> getCurrentPledgeAgreementsForPledgor(long pledgorId, String pervPosl){
        return repositoryPledgeAgreement.findByPledgorAndPervPoslEqualsAndStatusPAEquals(repositoryClient.getOne(pledgorId), pervPosl, "открыт");
    }

    public synchronized int countOfCurrentPledgeAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        return repositoryPledgeAgreement.countAllByPledgorInAndStatusPAEquals(pledgors, "открыт");
    }

    public synchronized int countOfPervCurrentPledgeAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        return repositoryPledgeAgreement.countAllByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors, "перв", "открыт");
    }

    public synchronized List<PledgeAgreement> getPervCurrentPledgeAgreementsForEmployeee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        return  repositoryPledgeAgreement.findByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors,  "перв", "открыт");
    }

    public synchronized int countOfPoslCurrentPledgeAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        return repositoryPledgeAgreement.countAllByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors, "посл", "открыт");
    }

    public synchronized int countOfLoanAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> loaners = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.countAllByLoanerInAndStatusLAEquals(loaners, "открыт");
    }

    public synchronized int countOfMonitoringNotDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getPervCurrentPledgeAgreementsForEmployeee(employeeId);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList){
            int count = repositoryPledgeSubject.countByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate, secondDate);
            if(count > 0)
                countOfMonitoring += 1;
        }

        return countOfMonitoring;
    }

    public synchronized int countOfMonitoringIsDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getPervCurrentPledgeAgreementsForEmployeee(employeeId);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList){
            int count = repositoryPledgeSubject.countByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate, secondDate);
            if(count > 0)
                countOfMonitoring += 1;
        }

        return countOfMonitoring;
    }
    public synchronized int countOfMonitoringOverdue(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getPervCurrentPledgeAgreementsForEmployeee(employeeId);
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList){
            int count = repositoryPledgeSubject.countByPledgeAgreementsAndDateMonitoringBefore(pa, beforeDate);
            if(count > 0)
                countOfMonitoring += 1;
        }

        return countOfMonitoring;
    }

    public synchronized List<PledgeAgreement> getPledgeAgreementWithMonitoringNotDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getPervCurrentPledgeAgreementsForEmployeee(employeeId);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList){
            int count = repositoryPledgeSubject.countByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate, secondDate);
            if(count > 0)
                pledgeAgreementListWithMonitoringNotDone.add(pa);
        }

        return pledgeAgreementListWithMonitoringNotDone;
    }

    public synchronized List<PledgeAgreement> getPledgeAgreementWithMonitoringIsDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getPervCurrentPledgeAgreementsForEmployeee(employeeId);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList){
            int count = repositoryPledgeSubject.countByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate, secondDate);
            if(count > 0)
                pledgeAgreementListWithMonitoringNotDone.add(pa);
        }

        return pledgeAgreementListWithMonitoringNotDone;
    }

    public synchronized List<PledgeAgreement> getPledgeAgreementWithMonitoringOverDue(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getPervCurrentPledgeAgreementsForEmployeee(employeeId);
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList){
            int count = repositoryPledgeSubject.countByPledgeAgreementsAndDateMonitoringBefore(pa, beforeDate);
            if(count > 0)
                pledgeAgreementListWithMonitoringNotDone.add(pa);
        }

        return pledgeAgreementListWithMonitoringNotDone;
    }

}
