package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.util.List;

public interface RepositoryLoanAgreement extends JpaRepository<LoanAgreement, Long>, JpaSpecificationExecutor<LoanAgreement> {
    List<LoanAgreement> findAllByLoanAgreementIdIn(List<Long> ids);
    List<LoanAgreement> findAllByPledgeAgreements(PledgeAgreement pledgeAgreement);
    List<LoanAgreement> findAllByPledgeAgreementsIn(List<PledgeAgreement> pledgeAgreement);
    List<LoanAgreement> findByPledgeAgreementsAndStatusLAEquals(PledgeAgreement pledgeAgreement, StatusOfAgreement statusLA);
    int countAllByClientInAndStatusLAEquals(List<Client> clients, StatusOfAgreement statusLA);
    List<LoanAgreement> findAllByClient(Client client);
    List<LoanAgreement> findByClientInAndStatusLAEquals(List<Client> clients, StatusOfAgreement statusLA);
    int countAllByStatusLAEquals(StatusOfAgreement statusOfAgreement);
    List<LoanAgreement> findAllByStatusLAEquals(StatusOfAgreement statusOfAgreement);

    @Query(nativeQuery = true, value = "select k.*\n" +
                                        "from kd k\n" +
                                        "join client_prime cp on k.loaner_id = cp.client_id\n" +
                                        "where cp.client_id = :clientId\n" +
                                        "and k.status = :statusLA")
    List<LoanAgreement> getLoanAgreementsByClient(@Param("clientId") Long clientId,
                                                    @Param("statusLA") String statusLA);


}
