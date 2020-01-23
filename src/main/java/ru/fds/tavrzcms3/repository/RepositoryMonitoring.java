package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.Monitoring;

import java.util.List;

public interface RepositoryMonitoring extends JpaRepository<Monitoring, Long> {
    @Query("select m from Monitoring m where m.pledgeSubject.pledgeSubjectId = :pledgeSubjectId")
    List<Monitoring> findByPledgeSubject(@Param("pledgeSubjectId") Long pledgeSubjectId, Sort sort);
}
