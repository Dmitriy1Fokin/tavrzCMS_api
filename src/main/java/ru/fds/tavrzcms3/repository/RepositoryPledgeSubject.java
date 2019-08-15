package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Date;
import java.util.List;

public interface RepositoryPledgeSubject extends JpaRepository<PledgeSubject, Long>, JpaSpecificationExecutor<PledgeSubject> {
    PledgeSubject findByPledgeSubjectId(long id);
    List<PledgeSubject> findByPledgeAgreementsIn(List<PledgeAgreement> pledgeAgreements);
    List<PledgeSubject> findByPledgeAgreements(PledgeAgreement pledgeEgreements);
    List<PledgeSubject> findByPledgeAgreementsAndDateMonitoringBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    int countByPledgeAgreementsAndDateMonitoringBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    List<PledgeSubject> findByPledgeAgreementsAndDateMonitoringBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
    int countByPledgeAgreementsAndDateMonitoringBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
    int countByPledgeAgreementsAndDateConclusionBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    int countByPledgeAgreementsAndDateConclusionBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
    boolean existsByPledgeAgreementsAndDateMonitoringBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    boolean existsByPledgeAgreementsAndDateMonitoringBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
    boolean existsByPledgeAgreementsAndDateConclusionBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    boolean existsByPledgeAgreementsAndDateConclusionBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
//    @Query("select ps from PledgeSubject ps where ps.pledgeAgreements.pledgor.clientId = :pledgorId")
//    List<PledgeSubject> findAll(@Param("pledgorId") long pledgorId);
}
