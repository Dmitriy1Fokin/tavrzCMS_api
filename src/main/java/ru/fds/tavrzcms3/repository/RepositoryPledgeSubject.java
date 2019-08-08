package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Date;
import java.util.List;

public interface RepositoryPledgeSubject extends JpaRepository<PledgeSubject, Long> {
    PledgeSubject findByPledgeSubjectId(long id);
    List<PledgeSubject> findByPledgeAgreements(List<PledgeAgreement> pledgeAgreements);
    List<PledgeSubject> findByPledgeAgreements(PledgeAgreement pledgeEgreements);
    List<PledgeSubject> findByPledgeAgreementsAndDateMonitoringBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    int countByPledgeAgreementsAndDateMonitoringBetween(PledgeAgreement pledgeAgreement, Date firstDate, Date secondDate);
    List<PledgeSubject> findByPledgeAgreementsAndDateMonitoringBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
    int countByPledgeAgreementsAndDateMonitoringBefore(PledgeAgreement pledgeAgreement, Date beforeDate);
}
