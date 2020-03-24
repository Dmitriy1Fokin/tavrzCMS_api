package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Pageable;
import ru.fds.tavrzcms3.domain.LoanAgreement;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LoanAgreementService {
    Optional<LoanAgreement> getLoanAgreementById(Long loanAgreementId);
    List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(Long pledgeAgreementId);
    List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(Long pledgeAgreementId);
    List<LoanAgreement> getAllLoanAgreementsByPledgeAgreements(List<Long> pledgeAgreementIds);
    List<LoanAgreement> getAllCurrentLoanAgreements(Pageable pageable);
    Integer getCountOfCurrentLoanAgreements();
    List<LoanAgreement> getCurrentLoanAgreementsByEmployee(Pageable pageable, Long employeeId);
    Integer getCountOfCurrentLoanAgreementsByEmployee(Long employeeId);
    List<LoanAgreement> getCurrentLoanAgreementsByLoaner(Long clientId);
    List<LoanAgreement> getClosedLoanAgreementsByLoaner(Long clientId);
    List<LoanAgreement> getLoanAgreementsByNumLA(String numLA);
    List<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam, Pageable pageable) throws ReflectiveOperationException;
    List<LoanAgreement> getNewLoanAgreementsFromFile(File file) throws IOException;
    List<LoanAgreement> getCurrentLoanAgreementsFromFile(File file) throws IOException;
    LoanAgreement insertLoanAgreement(LoanAgreement loanAgreement);
    LoanAgreement updateLoanAgreement(LoanAgreement loanAgreement);
    List<LoanAgreement> insertLoanAgreements(List<LoanAgreement> loanAgreementList);
    List<LoanAgreement> updateLoanAgreements(List<LoanAgreement> loanAgreementList);
}
