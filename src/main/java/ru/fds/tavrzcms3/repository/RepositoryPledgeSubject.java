package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.PledgeEgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.List;

public interface RepositoryPledgeSubject extends JpaRepository<PledgeSubject, Long> {
    PledgeSubject findByPledgeSubjectId(long id);
    List<PledgeSubject> findByPledgeEgreements(List<PledgeEgreement> pledgeEgreements);
    List<PledgeSubject> findByPledgeEgreements(PledgeEgreement pledgeEgreements);
}