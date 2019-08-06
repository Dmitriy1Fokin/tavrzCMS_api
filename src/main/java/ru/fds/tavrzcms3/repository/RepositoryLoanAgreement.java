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
    LoanAgreement findByNumLE (String numLE);
    List<LoanAgreement> findByDateBeginLE (Date dateBeginLE);
    List<LoanAgreement> findByLoaner (Client loaner);
    List<LoanAgreement> findByStatusLE (String statusLE);
    List<LoanAgreement> findByAmountLE (double amountLE);
    List<LoanAgreement> findByAmountLEBetween (double x, double y);
    List<LoanAgreement> findByAmountLELessThanEqual (double amountLE);
    List<LoanAgreement> findByAmountLEGreaterThanEqual (double amountLE);
    List<LoanAgreement> findByDebtLE (double debtLE);
    List<LoanAgreement> findByDebtLEBetween (double x, double y);
    List<LoanAgreement> findByDebtLELessThanEqual (double debtLE);
    List<LoanAgreement> findByDebtLEGreaterThanEqual (double debtLE);
    List<LoanAgreement> findByInterestRateLE (double interestRateLE);
    List<LoanAgreement> findByInterestRateLEBetween (double x, double y);
    List<LoanAgreement> findByInterestRateLELessThanEqual (double interestRateLE);
    List<LoanAgreement> findByInterestRateLEGreaterThanEqual (double interestRateLE);
    List<LoanAgreement> findByPfo (byte pfo);
    List<LoanAgreement> findByPfoBetween (byte x, byte y);
    List<LoanAgreement> findByQualityCategory (byte qualityCategory);
    List<LoanAgreement> findByQualityCategoryBetween (byte x, byte y);
    List<LoanAgreement> findByDateEndLE (Date dateEndLE);
    List<LoanAgreement> findByPledgeAgreementsAndStatusLEEquals(PledgeAgreement pledgeAgreement, String statusLA);
    int countAllByPledgeAgreementsAndStatusLEEquals(PledgeAgreement pledgeAgreement, String statusLA);
    int countAllByLoanerAndStatusLEEquals(Client loaner, String statusLA);
    int countAllByLoanerInAndStatusLEEquals(List<Client> clients, String statusLA);
    List<LoanAgreement> findByLoanerAndStatusLEEquals(Client loaner, String statusLA);
}
