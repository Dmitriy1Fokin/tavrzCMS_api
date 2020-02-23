package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    List<PledgeAgreement> findAllByClient(Client client);
    List<PledgeAgreement> findByClientInAndPervPoslEqualsAndStatusPAEquals(List<Client> clients, TypeOfPledgeAgreement pervPosl, StatusOfAgreement statusPA);
    Page<PledgeAgreement> findAllByNumPAContainingIgnoreCase(Pageable pageable, String numPA);
    Page<PledgeAgreement> findAllByStatusPAEquals(Pageable pageable, StatusOfAgreement statusPA);
    Integer countAllByStatusPAEquals(StatusOfAgreement statusPA);
    Page<PledgeAgreement> findAllByStatusPAEqualsAndPervPoslEquals(Pageable pageable, StatusOfAgreement statusPA, TypeOfPledgeAgreement typeOfPledgeAgreement);
    Integer countAllByStatusPAEqualsAndPervPoslEquals(StatusOfAgreement statusPA, TypeOfPledgeAgreement typeOfPledgeAgreement);

    @Query("select pp.pledgeAgreement from PaJoinPs pp where pp.pledgeSubject = :pledgeSubject")
    List<PledgeAgreement> findByPledgeSubject(@Param("pledgeSubject") PledgeSubject pledgeSubject);

    @Query("select lp.pledgeAgreement from LaJoinPa lp where lp.loanAgreement = :loanAgreement")
    List<PledgeAgreement> findByLoanAgreement(@Param("loanAgreement") LoanAgreement loanAgreement);

    @Query("select lp.pledgeAgreement from LaJoinPa lp where lp.loanAgreement.loanAgreementId = :loanAgreementId and lp.pledgeAgreement.statusPA = :statusPA")
    List<PledgeAgreement> findByLoanAgreementAndStatusPA(@Param("loanAgreementId") Long loanAgreementId,
                                                         @Param("statusPA") StatusOfAgreement statusOfAgreement);

    @Query(nativeQuery = true, value = "select distinct ps.date_conclusion " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = :pledgeAgreementId")
    List<Date> getDatesOfConclusion(@Param("pledgeAgreementId") Long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select distinct ps.date_monitoring " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = :pledgeAgreementId")
    List<Date> getDatesOMonitorings(@Param("pledgeAgreementId") Long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select distinct ps.status_monitoring " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = :pledgeAgreementId")
    List<String> getResultsOfMonitoring(@Param("pledgeAgreementId") Long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select distinct ps.type_of_collateral " +
                                        "from pledge_subject as ps " +
                                        "join dz_ps as dzps on dzps.pledge_subject_id = ps.pledge_subject_id " +
                                        "join dz as d on d.dz_id = dzps.dz_id " +
                                        "where d.dz_id = :pledgeAgreementId")
    List<String> getTypeOfCollateral(@Param("pledgeAgreementId") Long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select ps.name\n" +
                                        "from pledge_subject ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "where d.dz_id = :pledgeAgreementId " +
                                        "limit 5")
    List<String> getBriefInfoAboutCollateral(@Param("pledgeAgreementId") Long pledgeAgreementId);

    @Query(nativeQuery = true, value = "select d.*\n" +
                                        "from pledge_subject ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "where ps.pledge_subject_id = :pledgeSubjectId")
    List<PledgeAgreement> getPledgeAgreementByPLedgeSubject(@Param("pledgeSubjectId") Long pledgeSubjectId);

    @Query(nativeQuery = true, value = "select d.* " +
                                        "from employee as emp " +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id " +
                                        "join dz as d on d.pledgor_id = cp.client_id " +
                                        "where d.status = 'открыт' " +
                                        "and d.perv_posl = :pervPosl " +
                                        "and emp.employee_id = :employeeId " +
                                        "order by d.pledgor_id")
    Page<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(Pageable pageable,
                                                                @Param("employeeId") Long employeeId,
                                                                @Param("pervPosl") String pervPosl);

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id) " +
                                        "from employee as emp " +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id " +
                                        "join dz as d on d.pledgor_id = cp.client_id " +
                                        "where d.status = 'открыт' " +
                                        "and d.perv_posl = :pervPosl " +
                                        "and emp.employee_id = :employeeId ")
    Integer getCountOfCurrentPledgeAgreementsForEmployee(@Param("employeeId") Long employeeId,
                                                         @Param("pervPosl") String pervPosl);

    @Query(nativeQuery = true, value = "select d.* " +
                                        "from employee as emp " +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id " +
                                        "join dz as d on d.pledgor_id = cp.client_id " +
                                        "where d.status = 'открыт' " +
                                        "and emp.employee_id = :employeeId " +
                                        "order by d.pledgor_id")
    Page<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(Pageable pageable,
                                                                @Param("employeeId") Long employeeId);

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id) " +
                                        "from employee as emp " +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id " +
                                        "join dz as d on d.pledgor_id = cp.client_id " +
                                        "where d.status = 'открыт' " +
                                        "and emp.employee_id = :employeeId ")
    Integer getCountOfCurrentPledgeAgreementsForEmployee(@Param("employeeId") Long employeeId);

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

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id)\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_monitoring between :firstDate and :secondDate")
    Integer getCountPledgeAgreementWithMonitoringBetweenDates(@Param("employeeId") Long employeeId,
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
                                        "and ps.date_monitoring < :firstDate")
    Integer getCountPledgeAgreementWithMonitoringLessDate(@Param("employeeId") Long employeeId,
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

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id)\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_conclusion between :firstDate and :secondDate")
    Integer getCountPledgeAgreementWithConclusionsBetweenDates(@Param("employeeId") Long employeeId,
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

    @Query(nativeQuery = true, value = "select count(distinct d.dz_id)\n" +
                                        "from pledge_subject as ps\n" +
                                        "join dz_ps dp on ps.pledge_subject_id = dp.pledge_subject_id\n" +
                                        "join dz d on dp.dz_id = d.dz_id\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "join employee e on cp.employee_id = e.employee_id\n" +
                                        "where e.employee_id = :employeeId\n" +
                                        "and ps.date_conclusion < :firstDate")
    Integer getCountPledgeAgreementWithConclusionsLessDate(@Param("employeeId") Long employeeId,
                                                                         @Param("firstDate") LocalDate firstDate);

    @Query(nativeQuery = true, value = "select d.*\n" +
                                        "from dz d\n" +
                                        "join client_prime cp on d.pledgor_id = cp.client_id\n" +
                                        "where cp.client_id = :clientId\n" +
                                        "and d.status = :statusPA")
    List<PledgeAgreement> getPledgeAgreementsByClient(@Param("clientId") Long clientId,
                                                      @Param("statusPA") String statusPA);

}
