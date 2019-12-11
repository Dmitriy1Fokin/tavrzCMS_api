package ru.fds.tavrzcms3.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PledgeAgreementServiceTest {

    @Autowired
    PledgeAgreementService pledgeAgreementService;
    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;
    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;
    @Autowired
    RepositoryEmployee repositoryEmployee;
    @Autowired
    RepositoryClient repositoryClient;



    @Test
    public void getPledgeAgreementsByNumPA() {
        PledgeAgreement pledgeAgreementTest = repositoryPledgeAgreement.findById(622L).orElseThrow(()-> new NullPointerException("Нет такого ДЗ!"));

        List<PledgeAgreement> pledgeAgreementResult = pledgeAgreementService.getPledgeAgreementsByNumPA(pledgeAgreementTest.getNumPA());

        assertEquals(pledgeAgreementTest.getPledgeAgreementId(), pledgeAgreementResult.get(0).getPledgeAgreementId());
    }

    @Test
    public void getDatesOfConclusion() {
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.findById(126L).orElseThrow(()-> new NullPointerException("Нет такого ДЗ!"));

        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<Date> datesTest = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects)
            if(!datesTest.contains(ps.getDateConclusion()))
                datesTest.add(ps.getDateConclusion());

        List<Date> datesResult = pledgeAgreementService.getDatesOfConclusion(pledgeAgreement);

        assertEquals(datesTest.size(), datesResult.size());
        assertTrue(datesTest.containsAll(datesResult));
    }

    @Test
    public void getDatesOfMonitoring() {
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.findById(213L).orElseThrow(()-> new NullPointerException("Нет такого ДЗ!"));

        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<Date> datesTest = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects)
            if(!datesTest.contains(ps.getDateMonitoring()))
                datesTest.add(ps.getDateMonitoring());

        List<Date> datesResult = pledgeAgreementService.getDatesOfMonitoring(pledgeAgreement);

        assertEquals(datesTest.size(), datesResult.size());
        assertTrue(datesTest.containsAll(datesResult));
    }

    @Test
    public void getResultsOfMonitoring() {
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.findById(214L).orElseThrow(()-> new NullPointerException("Нет такого ДЗ!"));

        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<String> monitoringResultTest = new ArrayList<>();
        for (PledgeSubject ps : pledgeSubjects)
            if(!monitoringResultTest.contains(ps.getStatusMonitoring()))
                monitoringResultTest.add(ps.getStatusMonitoring().getTranslate());

        List<String> monitoringResultResult = pledgeAgreementService.getResultsOfMonitoring(pledgeAgreement);

        assertEquals(monitoringResultTest.size(), monitoringResultResult.size());
    }

    @Test
    public void getPledgeAgreement() {
        PledgeAgreement pledgeAgreementTest = repositoryPledgeAgreement.findById(123L).orElseThrow(()-> new NullPointerException("Нет такого ДЗ!"));

        PledgeAgreement pledgeAgreementResult = pledgeAgreementService.getPledgeAgreementById(123L).get();

        assertEquals(pledgeAgreementTest.getPledgeAgreementId(), pledgeAgreementResult.getPledgeAgreementId());
    }

    @Test
    public void getTypeOfCollateral() {
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.findById(214L).orElseThrow(()-> new NullPointerException("Нет такого ДЗ!"));

        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<String> typeOfCollateralTest = new ArrayList<>();
        for (PledgeSubject ps : pledgeSubjects)
            if(!typeOfCollateralTest.contains(ps.getTypeOfCollateral()))
                typeOfCollateralTest.add(ps.getTypeOfCollateral().getTranslate());

        List<String> typeOfCollateralResult = pledgeAgreementService.getTypeOfCollateral(pledgeAgreement);

        assertEquals(typeOfCollateralTest.size(), typeOfCollateralResult.size());
        assertTrue(typeOfCollateralTest.containsAll(typeOfCollateralResult));
    }

    @Test
    public void countOfCurrentLoanAgreementsForPledgeAgreement() {
        Employee employee = repositoryEmployee.findById(1L).orElseThrow(()-> new RuntimeException("Нет такого сотрудника"));
        TypeOfPledgeAgreement pervPosl = TypeOfPledgeAgreement.PERV;

        List<Client> clientList = repositoryClient.findByEmployee(employee);
        List<PledgeAgreement> pledgeAgreementsTest = repositoryPledgeAgreement.findByClientInAndPervPoslEqualsAndStatusPAEquals(
                clientList,
                pervPosl,
                StatusOfAgreement.OPEN);

        List<PledgeAgreement> pledgeAgreementsResult = pledgeAgreementService.getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), pervPosl);

        assertEquals(pledgeAgreementsTest.size(), pledgeAgreementsResult.size());
    }

    @Test
    public void getCurrentLoanAgreementsForPledgeAgreement() {
    }

    @Test
    public void countOfClosedLoanAgreementsForPledgeAgreement() {
    }

    @Test
    public void getClosedLoanAgreementsForPledgeAgreement() {
    }

    @Test
    public void getCurrentPledgeAgreementsForEmployee() {
    }

    @Test
    public void getCurrentPledgeAgreementsForEmployee1() {
    }

    @Test
    public void getCurrentPledgeAgreementsForEmployee2() {
    }

    @Test
    public void countOfCurrentPledgeAgreementForEmployee() {
    }

    @Test
    public void countOfCurrentPledgeAgreementForEmployee1() {
    }

    @Test
    public void countOfMonitoringNotDone() {
    }

    @Test
    public void countOfMonitoringIsDone() {
    }

    @Test
    public void countOfMonitoringOverdue() {
    }

    @Test
    public void getPledgeAgreementWithMonitoringNotDone() {
    }

    @Test
    public void getPledgeAgreementWithMonitoringIsDone() {
    }

    @Test
    public void getPledgeAgreementWithMonitoringOverDue() {
    }

    @Test
    public void countOfConclusionNotDone() {
    }

    @Test
    public void countOfConclusionIsDone() {
    }

    @Test
    public void countOfConclusionOverdue() {
    }

    @Test
    public void getPledgeAgreementWithConclusionNotDone() {
    }

    @Test
    public void getPledgeAgreementWithConclusionIsDone() {
    }

    @Test
    public void getPledgeAgreementWithConclusionOverDue() {
    }

    @Test
    public void getPledgeAgreementFromSearch() {
    }

    @Test
    public void getCurrentPledgeAgreementsByPledgor() {
    }

    @Test
    public void updateInsertPledgeAgreement() {
    }

    @Test
    public void updateInsertPledgeAgreementList() {
    }
}