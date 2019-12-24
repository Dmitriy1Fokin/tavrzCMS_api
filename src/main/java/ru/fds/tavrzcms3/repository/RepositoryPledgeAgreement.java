package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface RepositoryPledgeAgreement extends JpaRepository<PledgeAgreement, Long>, JpaSpecificationExecutor<PledgeAgreement> {
    List<PledgeAgreement> findAllByPledgeAgreementIdIn(Collection<Long> ids);
    List<PledgeAgreement> findAllByPledgeSubjects(PledgeSubject pledgeSubject);
    List<PledgeAgreement> findAllByClient(Client client);
    int countAllByClientInAndStatusPAEquals(List<Client> clients, StatusOfAgreement statusPA);
    int countAllByClientInAndPervPoslEqualsAndStatusPAEquals(List<Client> clients, TypeOfPledgeAgreement perv, StatusOfAgreement statusPA);
    List<PledgeAgreement> findByClientAndStatusPA(Client client, StatusOfAgreement statusPA);
    List<PledgeAgreement> findByLoanAgreements(LoanAgreement loanAgreement);
    List<PledgeAgreement> findByLoanAgreementsAndStatusPAEquals(LoanAgreement loanAgreement, StatusOfAgreement statusPA);
    List<PledgeAgreement> findByClientInAndPervPoslEqualsAndStatusPAEquals(List<Client> clients, TypeOfPledgeAgreement pervPosl, StatusOfAgreement statusPA);
    List<PledgeAgreement> findAllByNumPAContainingIgnoreCase(String numPA);
    List<PledgeAgreement> findAllByStatusPAEquals(StatusOfAgreement statusPA);
    List<PledgeAgreement> findAllByStatusPAEqualsAndPervPoslEquals(StatusOfAgreement statusPA, TypeOfPledgeAgreement typeOfPledgeAgreement);
    int countAllByStatusPAEquals(StatusOfAgreement statusPA);
    int countAllByStatusPAEqualsAndPervPoslEquals(StatusOfAgreement statusPA, TypeOfPledgeAgreement typeOfPledgeAgreement);

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

    @Query(nativeQuery = true, value = "select d.* " +
                                        "from employee as emp " +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id " +
                                        "join dz as d on d.pledgor_id = cp.client_id " +
                                        "where d.status = 'открыт' " +
                                        "and emp.employee_id = ?1 " +
                                        "order by d.pledgor_id")
    List<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(long employee);

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id)\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_monitoring between :firstDate and :secondDate")
    int countOfMonitoringBetweenDates(@Param("employeeId") Long employeeId,
                                      @Param("firstDate") LocalDate firstDate,
                                      @Param("secondDate") LocalDate secondDate);

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id)\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_monitoring < :firstDate")
    int countOfMonitoringLessDate(@Param("employeeId") Long employeeId,
                                  @Param("firstDate") LocalDate firstDate);

    @Query(nativeQuery = true, value = "select distinct d.*\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_monitoring between :firstDate and :secondDate")
    List<PledgeAgreement> getPledgeAgreementWithMonitoringBetweenDates(@Param("employeeId") Long employeeId,
                                                                       @Param("firstDate") LocalDate firstDate,
                                                                       @Param("secondDate") LocalDate secondDate);

    @Query(nativeQuery = true, value = "select distinct d.*\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_monitoring < :firstDate")
    List<PledgeAgreement> getPledgeAgreementWithMonitoringLessDate(@Param("employeeId") Long employeeId,
                                                                    @Param("firstDate") LocalDate firstDate);

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id)\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_conclusion between :firstDate and :secondDate")
    int countOfConclusionsBetweenDates(@Param("employeeId") Long employeeId,
                                       @Param("firstDate") LocalDate firstDate,
                                       @Param("secondDate") LocalDate secondDate);

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id)\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_conclusion < :firstDate")
    int countOfConclusionsLessDate(@Param("employeeId") Long employeeId,
                                   @Param("firstDate") LocalDate firstDate);

    @Query(nativeQuery = true, value = "select distinct d.*\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_conclusion between :firstDate and :secondDate")
    List<PledgeAgreement> getPledgeAgreementWithConclusionsBetweenDates(@Param("employeeId") Long employeeId,
                                                                        @Param("firstDate") LocalDate firstDate,
                                                                        @Param("secondDate") LocalDate secondDate);

    @Query(nativeQuery = true, value = "select distinct d.*\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_conclusion < :firstDate")
    List<PledgeAgreement> getPledgeAgreementWithConclusionsLessDate(@Param("employeeId") Long employeeId,
                                                                    @Param("firstDate") LocalDate firstDate);

}
