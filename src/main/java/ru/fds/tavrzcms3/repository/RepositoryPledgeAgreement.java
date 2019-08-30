package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.util.List;

public interface RepositoryPledgeAgreement extends JpaRepository<PledgeAgreement, Long>, JpaSpecificationExecutor<PledgeAgreement> {
    PledgeAgreement findByPledgeAgreementId(long pledgeAgreementId);
    List<PledgeAgreement> findByPledgor (Client pledgor);
    int countAllByPledgorInAndStatusPAEquals(List<Client> pledgors, String statusPA);
    int countAllByPledgorInAndPervPoslEqualsAndStatusPAEquals(List<Client> pledgors, String perv, String statusPA);
    int countAllByPledgorAndStatusPAEquals(Client pledgor, String statusPA);
    List<PledgeAgreement> findByPledgorAndStatusPA(Client pledgor, String statusPA);
    List<PledgeAgreement> findByLoanAgreementsAndStatusPAEquals(LoanAgreement loanAgreement, String statusPA);
    List<PledgeAgreement> findByPledgorInAndPervPoslEqualsAndStatusPAEquals(List<Client> pledgors, String pervPosl, String statusPA, Sort sort);
    Page<PledgeAgreement> findByPledgorInAndPervPoslEqualsAndStatusPAEquals(List<Client> pledgors, String pervPosl, String statusPA, Pageable pageable);
    List<PledgeAgreement> findByPledgorInAndStatusPAEquals(List<Client> pledgors, String statusPA, Sort sort);
    Page<PledgeAgreement> findByPledgorInAndStatusPAEquals(List<Client> pledgors, String statusPA, Pageable pageable);
    Page<PledgeAgreement> findAll(Specification specification, Pageable pageable);
}
