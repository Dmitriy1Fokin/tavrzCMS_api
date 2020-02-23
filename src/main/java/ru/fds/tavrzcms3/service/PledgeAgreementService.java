package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PledgeAgreementService {
    Optional<PledgeAgreement> getPledgeAgreementById(Long pledgeAgreementId);
    List<PledgeAgreement> getPledgeAgreementsByIds(List<Long> ids);
    List<PledgeAgreement> getAllCurrentPledgeAgreements();
    Integer getCountCurrentPledgeAgreements();
    List<PledgeAgreement> getAllCurrentPledgeAgreements(TypeOfPledgeAgreement typeOfPledgeAgreement);
    Integer getCountCurrentPledgeAgreements(TypeOfPledgeAgreement typeOfPledgeAgreement);
    List<PledgeAgreement> getPledgeAgreementsByNumPA(String numPA);
    List<LocalDate> getDatesOfConclusion(PledgeAgreement pledgeAgreement);
    List<LocalDate> getDatesOfMonitoring(PledgeAgreement pledgeAgreement);
    List<String> getResultsOfMonitoring(PledgeAgreement pledgeAgreement);
    List<String> getTypeOfCollateral(PledgeAgreement pledgeAgreement);
    List<String> getBriefInfoAboutCollateral(PledgeAgreement pledgeAgreement);
    List<PledgeAgreement> getAllPledgeAgreementByPLedgeSubject(Long pledgeSubjectId);
    List<PledgeAgreement> getCurrentPledgeAgreementsByEmployee(Long employeeId, TypeOfPledgeAgreement pervPosl);
    Integer getCountOfCurrentPledgeAgreementsByEmployee(Long employeeId, TypeOfPledgeAgreement pervPosl);
    List<PledgeAgreement> getCurrentPledgeAgreementsByEmployee(Long employeeId);
    Integer getCountOfCurrentPledgeAgreementsByEmployee(Long employeeId);
    List<PledgeAgreement> getPledgeAgreementWithMonitoringNotDone(Long employeeId);
    Integer getCountPledgeAgreementWithMonitoringNotDone(Long employeeId);
    List<PledgeAgreement> getPledgeAgreementWithMonitoringIsDone(Long employeeId);
    Integer getCountPledgeAgreementWithMonitoringIsDone(Long employeeId);
    List<PledgeAgreement> getPledgeAgreementWithMonitoringOverDue(Long employeeId);
    Integer getCountPledgeAgreementWithMonitoringOverdue(Long employeeId);
    List<PledgeAgreement> getPledgeAgreementWithConclusionNotDone(Long employeeId);
    Integer getCountPledgeAgreementWithConclusionNotDone(Long employeeId);
    List<PledgeAgreement> getPledgeAgreementWithConclusionIsDone(Long employeeId);
    Integer getCountPledgeAgreementWithConclusionIsDone(Long employeeId);
    List<PledgeAgreement> getPledgeAgreementWithConclusionOverdue(Long employeeId);
    Integer getCountPledgeAgreementWithConclusionOverdue(Long employeeId);
    List<PledgeAgreement> getPledgeAgreementFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException;
    List<PledgeAgreement> getCurrentPledgeAgreementsByPledgor(Long clientId);
    List<PledgeAgreement> getClosedPledgeAgreementsByPledgor(Long clientId);
    List<PledgeAgreement> getCurrentPledgeAgreementsByLoanAgreement(Long loanAgreementId);
    List<PledgeAgreement> getClosedPledgeAgreementsByLoanAgreement(Long loanAgreementId);
    List<PledgeAgreement> getNewPledgeAgreementsFromFile(File file) throws IOException;
    List<PledgeAgreement> getCurrentPledgeAgreementsFromFile(File file) throws IOException;
    PledgeAgreement insertPledgeAgreement(PledgeAgreement pledgeAgreement, List<Long> loanAgreementsIds);
    PledgeAgreement updatePledgeAgreement(PledgeAgreement pledgeAgreement, List<Long> loanAgreementsIds);
    List<PledgeAgreement> insertPledgeAgreements(List<PledgeAgreement> pledgeAgreementList,
                                                       List<List<LoanAgreement>> loanAgreementsList);
    List<PledgeAgreement> updatePledgeAgreements(List<PledgeAgreement> pledgeAgreementList,
                                                       List<List<LoanAgreement>> loanAgreementsList);
    PledgeAgreement withdrawPledgeSubjectFromPledgeAgreement(Long pledgeAgreementId, Long pledgeSubjectId);
    PledgeAgreement insertCurrentPledgeSubjectsInPledgeAgreement(Long pledgeAgreementId, List<Long> pledgeSubjectsIds);
}
