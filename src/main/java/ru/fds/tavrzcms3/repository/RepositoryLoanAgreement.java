package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.util.List;

public interface RepositoryLoanAgreement extends JpaRepository<LoanAgreement, Long>, JpaSpecificationExecutor<LoanAgreement> {
    LoanAgreement findByLoanAgreementId(long loanAgreementId);
    List<LoanAgreement> findByPledgeAgreementsAndStatusLAEquals(PledgeAgreement pledgeAgreement, String statusLA);
    int countAllByPledgeAgreementsAndStatusLAEquals(PledgeAgreement pledgeAgreement, String statusLA);
    int countAllByClientAndStatusLAEquals(Client client, String statusLA);
    int countAllByClientInAndStatusLAEquals(List<Client> clients, String statusLA);
    List<LoanAgreement> findByClientAndStatusLAEquals(Client client, String statusLA);
    Page<LoanAgreement> findByClientInAndStatusLAEquals(List<Client> clients, String statusLA, Pageable pageable);
    Page<LoanAgreement> findAll(Specification specification, Pageable pageable);

}
