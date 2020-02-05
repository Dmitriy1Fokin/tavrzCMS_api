package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.LoanAgreement;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LoanAgreementService {
    Optional<LoanAgreement> getLoanAgreementById(long loanAgreementId);

    List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(Long pledgeAgreementId);

    List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(Long pledgeAgreementId);

    List<LoanAgreement> getAllCurrentLoanAgreements();

    List<LoanAgreement> getCurrentLoanAgreementsByEmployee(Long employeeId);

    List<LoanAgreement> getCurrentLoanAgreementsByLoaner(Long clientId);

    List<LoanAgreement> getClosedLoanAgreementsByLoaner(Long clientId);

    List<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException;

    List<LoanAgreement> getNewLoanAgreementsFromFile(File file) throws IOException;

    List<LoanAgreement> getCurrentLoanAgreementsFromFile(File file) throws IOException;

    LoanAgreement insertLoanAgreement(LoanAgreement loanAgreement);

    LoanAgreement updateLoanAgreement(LoanAgreement loanAgreement);

    List<LoanAgreement> insertLoanAgreements(List<LoanAgreement> loanAgreementList);

    List<LoanAgreement> updateLoanAgreements(List<LoanAgreement> loanAgreementList);
}
