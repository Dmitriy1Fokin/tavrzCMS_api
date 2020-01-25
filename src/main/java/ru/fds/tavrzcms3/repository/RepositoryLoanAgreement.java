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
    List<LoanAgreement> findAllByClient(Client client);
    List<LoanAgreement> findAllByStatusLAEquals(StatusOfAgreement statusOfAgreement);

    @Query("select lp.loanAgreement from LaJoinPa lp where lp.pledgeAgreement = :pledgeAgreement")
    List<LoanAgreement> findByPledgeAgreement(@Param("pledgeAgreement")PledgeAgreement pledgeAgreement);

    @Query("select lp.loanAgreement from LaJoinPa lp where lp.pledgeAgreement.pledgeAgreementId = :pledgeAgreementId and lp.loanAgreement.statusLA = :statusLA")
    List<LoanAgreement> findByPledgeAgreementAndStatusLA(@Param("pledgeAgreementId") Long pledgeAgreementId,
                                                          @Param("statusLA") StatusOfAgreement statusOfAgreement);

    @Query(nativeQuery = true, value = "select k.*\n" +
                                        "from kd k\n" +
                                        "join client_prime cp on k.loaner_id = cp.client_id\n" +
                                        "where cp.client_id = :clientId\n" +
                                        "and k.status = :statusLA")
    List<LoanAgreement> getLoanAgreementsByClient(@Param("clientId") Long clientId,
                                                  @Param("statusLA") String statusLA);

    @Query(nativeQuery = true, value = "select k.*\n" +
                                        "from kd k\n" +
                                        "join client_prime cp on k.loaner_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = 10\n" +
                                        "and k.status = 'открыт'")
    List<LoanAgreement> getLoanAgreementByEmployee(@Param("employeeId") Long employeeId,
                                                   @Param("statusLA") String statusLA);

}
