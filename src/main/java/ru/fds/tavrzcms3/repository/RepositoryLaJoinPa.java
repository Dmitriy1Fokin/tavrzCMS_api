package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.jointable.LaJoinPa;

import java.util.List;

public interface RepositoryLaJoinPa extends JpaRepository<LaJoinPa, Long> {
    void deleteByLoanAgreementAndPledgeAgreement(LoanAgreement loanAgreement, PledgeAgreement pledgeAgreement);
    void deleteByPledgeAgreement(PledgeAgreement pledgeAgreement);
    void deleteByPledgeAgreementIn(List<PledgeAgreement> pledgeAgreementList);
    void deleteByLoanAgreementInAndPledgeAgreement(List<LoanAgreement> loanAgreementList, PledgeAgreement pledgeAgreement);
    boolean existsByLoanAgreementAndPledgeAgreement(LoanAgreement loanAgreement, PledgeAgreement pledgeAgreement);
    List<LaJoinPa> findByPledgeAgreement(PledgeAgreement pledgeAgreement);
}
