package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanEgreement;

import java.util.Date;
import java.util.List;

public interface RepositoryLoanEgreement extends JpaRepository<LoanEgreement, Long> {
    LoanEgreement findByNumLE (String numLE);
    List<LoanEgreement> findByDateBeginLE (Date dateBeginLE);
    List<LoanEgreement> findByLoaner (Client loaner);
    List<LoanEgreement> findByStatusLE (String statusLE);
    List<LoanEgreement> findByAmountLE (double amountLE);
    List<LoanEgreement> findByAmountLEBetween (double x, double y);
    List<LoanEgreement> findByAmountLELessThanEqual (double amountLE);
    List<LoanEgreement> findByAmountLEGreaterThanEqual (double amountLE);
    List<LoanEgreement> findByDebtLE (double debtLE);
    List<LoanEgreement> findByDebtLEBetween (double x, double y);
    List<LoanEgreement> findByDebtLELessThanEqual (double debtLE);
    List<LoanEgreement> findByDebtLEGreaterThanEqual (double debtLE);
    List<LoanEgreement> findByInterestRateLE (double interestRateLE);
    List<LoanEgreement> findByInterestRateLEBetween (double x, double y);
    List<LoanEgreement> findByInterestRateLELessThanEqual (double interestRateLE);
    List<LoanEgreement> findByInterestRateLEGreaterThanEqual (double interestRateLE);
    List<LoanEgreement> findByPfo (byte pfo);
    List<LoanEgreement> findByPfoBetween (byte x, byte y);
    List<LoanEgreement> findByQualityCategory (byte qualityCategory);
    List<LoanEgreement> findByQualityCategoryBetween (byte x, byte y);
    List<LoanEgreement> findByDateEndLE (Date dateEndLE);
}
