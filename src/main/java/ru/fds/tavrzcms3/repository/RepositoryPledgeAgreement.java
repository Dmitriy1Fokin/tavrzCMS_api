package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Date;
import java.util.List;

public interface RepositoryPledgeAgreement extends JpaRepository<PledgeAgreement, Long> {
    PledgeAgreement findByPledgeAgreementId(long pledgeAgreementId);
    List<PledgeAgreement> findByNumPE (String numPE);
    List<PledgeAgreement> findByDateBeginPE (Date dateBeginPE);
    List<PledgeAgreement> findByDateEndPE (Date dateEndPE);
    List<PledgeAgreement> findByPervPosl (String pervPosl);
    List<PledgeAgreement> findByPledgor (Client pledgor);
    List<PledgeAgreement> findByStatusPE (String statusPE);
    List<PledgeAgreement> findByPledgorIn (List<Client> pledgors);
    int countAllByPledgorIn(List<Client> pledgors);
    int countAllByPledgorInAndStatusPEEquals(List<Client> pledgors, String statusPE);
    int countAllByPledgorInAndPervPoslEqualsAndStatusPEEquals(List<Client> pledgors, String perv, String statusPE);
    List<PledgeAgreement> findByPledgeSubjects(PledgeSubject pledgeSubject);
    List<PledgeAgreement> findByLoanAgreements(LoanAgreement loanAgreement);
    List<PledgeAgreement> findByLoanAgreementsAndStatusPEEquals(LoanAgreement loanAgreement, String statusPE);
    List<PledgeAgreement> findByPledgorAndPervPoslEquals(Client pledgor, String pervPosl);
}
