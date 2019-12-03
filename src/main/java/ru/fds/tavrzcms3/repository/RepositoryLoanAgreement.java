package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.util.List;

public interface RepositoryLoanAgreement extends JpaRepository<LoanAgreement, Long>, JpaSpecificationExecutor<LoanAgreement> {
    List<LoanAgreement> findAllByLoanAgreementIdIn(List<Long> ids);
    List<LoanAgreement> findAllByPledgeAgreements(PledgeAgreement pledgeAgreement);
    List<LoanAgreement> findByPledgeAgreementsAndStatusLAEquals(PledgeAgreement pledgeAgreement, StatusOfAgreement statusLA);
    int countAllByPledgeAgreementsAndStatusLAEquals(PledgeAgreement pledgeAgreement, StatusOfAgreement statusLA);
    int countAllByClientAndStatusLAEquals(Client client, StatusOfAgreement statusLA);
    int countAllByClientInAndStatusLAEquals(List<Client> clients, StatusOfAgreement statusLA);
    List<LoanAgreement> findByClientAndStatusLAEquals(Client client, StatusOfAgreement statusLA);
    List<LoanAgreement> findAllByClient(Client client);
    Page<LoanAgreement> findByClientInAndStatusLAEquals(List<Client> clients, StatusOfAgreement statusLA, Pageable pageable);
    Page<LoanAgreement> findAll(Specification specification, Pageable pageable);

}
