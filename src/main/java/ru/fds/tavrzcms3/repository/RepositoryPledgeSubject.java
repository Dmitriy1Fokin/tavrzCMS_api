package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RepositoryPledgeSubject extends JpaRepository<PledgeSubject, Long>, JpaSpecificationExecutor<PledgeSubject> {
    List<PledgeSubject> findAllByPledgeSubjectIdIn(List<Long> ids);
    List<PledgeSubject> findByPledgeAgreementsIn(List<PledgeAgreement> pledgeAgreements);
    List<PledgeSubject> findByPledgeAgreements(PledgeAgreement pledgeAgreements);
    boolean existsByPledgeAgreementsAndDateMonitoringBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    boolean existsByPledgeAgreementsAndDateMonitoringBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
    boolean existsByPledgeAgreementsAndDateConclusionBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    boolean existsByPledgeAgreementsAndDateConclusionBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
    Page<PledgeSubject> findAll(Specification specification, Pageable pageable);
    List<PledgeSubject> findAllByNameContainingIgnoreCase(String name);
    Optional<PledgeSubject> findByCostHistories(CostHistory costHistory);
}
