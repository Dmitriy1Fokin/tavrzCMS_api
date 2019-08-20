package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Date;
import java.util.List;

public interface RepositoryPledgeAgreement extends JpaRepository<PledgeAgreement, Long>, JpaSpecificationExecutor<PledgeAgreement> {
    PledgeAgreement findByPledgeAgreementId(long pledgeAgreementId);
    List<PledgeAgreement> findByNumPA (String numPA);
    List<PledgeAgreement> findByDateBeginPA (Date dateBeginPA);
    List<PledgeAgreement> findByDateEndPA (Date dateEndPA);
    List<PledgeAgreement> findByPervPosl (String pervPosl);
    List<PledgeAgreement> findByPledgor (Client pledgor);
    List<PledgeAgreement> findByStatusPA (String statusPA);
    List<PledgeAgreement> findByPledgorIn (List<Client> pledgors);
    int countAllByPledgorIn(List<Client> pledgors);
    int countAllByPledgorInAndStatusPAEquals(List<Client> pledgors, String statusPA);
    int countAllByPledgorInAndPervPoslEqualsAndStatusPAEquals(List<Client> pledgors, String perv, String statusPA);
    int countAllByPledgorAndStatusPAEquals(Client pledgor, String statusPA);
    List<PledgeAgreement> findByPledgorAndStatusPA(Client pledgor, String statusPA);
    List<PledgeAgreement> findByPledgeSubjects(PledgeSubject pledgeSubject);
    List<PledgeAgreement> findByLoanAgreements(LoanAgreement loanAgreement);
    List<PledgeAgreement> findByLoanAgreementsAndStatusPAEquals(LoanAgreement loanAgreement, String statusPA);
    List<PledgeAgreement> findByPledgorAndPervPoslEqualsAndStatusPAEquals(Client pledgor, String pervPosl, String statusPA);
    List<PledgeAgreement> findByPledgorInAndPervPoslEqualsAndStatusPAEquals(List<Client> pledgors, String pervPosl, String statusPA, Sort sort);
    List<PledgeAgreement> findByPledgorInAndStatusPAEquals(List<Client> pledgors, String statusPA, Sort sort);
}
