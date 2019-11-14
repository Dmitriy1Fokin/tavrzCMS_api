package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.util.Date;
import java.util.List;

public interface RepositoryPledgeAgreement extends JpaRepository<PledgeAgreement, Long>, JpaSpecificationExecutor<PledgeAgreement> {
    PledgeAgreement findByPledgeAgreementId(long pledgeAgreementId);
    List<PledgeAgreement> findByClient(Client client);
    int countAllByClientInAndStatusPAEquals(List<Client> clients, String statusPA);
    int countAllByClientInAndPervPoslEqualsAndStatusPAEquals(List<Client> clients, String perv, String statusPA);
    int countAllByClientAndStatusPAEquals(Client client, String statusPA);
    List<PledgeAgreement> findByClientAndStatusPA(Client client, String statusPA);
    List<PledgeAgreement> findByLoanAgreements(LoanAgreement loanAgreement);
    List<PledgeAgreement> findByLoanAgreementsAndStatusPAEquals(LoanAgreement loanAgreement, String statusPA);
    List<PledgeAgreement> findByClientInAndPervPoslEqualsAndStatusPAEquals(List<Client> clients, String pervPosl, String statusPA);
    Page<PledgeAgreement> findByClientInAndPervPoslEqualsAndStatusPAEquals(List<Client> clients, String pervPosl, String statusPA, Pageable pageable);
    List<PledgeAgreement> findByClientInAndStatusPAEquals(List<Client> clients, String statusPA, Sort sort);
    Page<PledgeAgreement> findByClientInAndStatusPAEquals(List<Client> clients, String statusPA, Pageable pageable);
    Page<PledgeAgreement> findAll(Specification specification, Pageable pageable);
    List<PledgeAgreement> findAllByNumPAContainingIgnoreCase(String numPA);

    @Query(nativeQuery = true, value = "select distinct ps.date_conclusion " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = ?1")
    List<Date> getDatesOfConclusion(long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select distinct ps.date_monitoring " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = ?1")
    List<Date> getDatesOMonitorings(long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select distinct ps.status_monitoring " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = ?1")
    List<String> getResultsOfMonitoring(long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select distinct ps.type_of_collateral " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = ?1")
    List<String> getTypeOfCollateral(long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select d.* " +
                                        "from employee as emp " +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id " +
                                        "join dz as d on d.pledgor_id = cp.client_id " +
                                        "where d.status = 'открыт' " +
                                        "and d.perv_posl = ?2 " +
                                        "and emp.employee_id = ?1 " +
                                        "order by d.pledgor_id")
    List<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(long employee, String pervPosl);

}
