package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.util.Date;
import java.util.List;

public interface RepositoryLoanAgreement extends JpaRepository<LoanAgreement, Long> {
    LoanAgreement findByLoanAgreementId(long loanAgreementId);
    LoanAgreement findByNumLA (String numLA);
    List<LoanAgreement> findByDateBeginLA (Date dateBeginLA);
    List<LoanAgreement> findByLoaner (Client loaner);
    List<LoanAgreement> findByStatusLA (String statusLA);
    List<LoanAgreement> findByAmountLA (double amountLA);
    List<LoanAgreement> findByAmountLABetween (double x, double y);
    List<LoanAgreement> findByAmountLALessThanEqual (double amountLA);
    List<LoanAgreement> findByAmountLAGreaterThanEqual (double amountLA);
    List<LoanAgreement> findByDebtLA (double debtLA);
    List<LoanAgreement> findByDebtLABetween (double x, double y);
    List<LoanAgreement> findByDebtLALessThanEqual (double debtLA);
    List<LoanAgreement> findByDebtLAGreaterThanEqual (double debtLA);
    List<LoanAgreement> findByInterestRateLA (double interestRateLA);
    List<LoanAgreement> findByInterestRateLABetween (double x, double y);
    List<LoanAgreement> findByInterestRateLALessThanEqual (double interestRateLA);
    List<LoanAgreement> findByInterestRateLAGreaterThanEqual (double interestRateLA);
    List<LoanAgreement> findByPfo (byte pfo);
    List<LoanAgreement> findByPfoBetween (byte x, byte y);
    List<LoanAgreement> findByQualityCategory (byte qualityCategory);
    List<LoanAgreement> findByQualityCategoryBetween (byte x, byte y);
    List<LoanAgreement> findByDateEndLA (Date dateEndLA);
    List<LoanAgreement> findByPledgeAgreementsAndStatusLAEquals(PledgeAgreement pledgeAgreement, String statusLA);
    int countAllByPledgeAgreementsAndStatusLAEquals(PledgeAgreement pledgeAgreement, String statusLA);
    int countAllByLoanerAndStatusLAEquals(Client loaner, String statusLA);
    int countAllByLoanerInAndStatusLAEquals(List<Client> clients, String statusLA);
    List<LoanAgreement> findByLoanerAndStatusLAEquals(Client loaner, String statusLA);
}
