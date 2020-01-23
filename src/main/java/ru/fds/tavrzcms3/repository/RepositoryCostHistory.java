package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.CostHistory;

import java.util.List;

public interface RepositoryCostHistory extends JpaRepository<CostHistory, Long> {
    List<CostHistory> findAllByCostHistoryIdIn(List<Long> ids);

    @Query("select ch from CostHistory ch where ch.pledgeSubject.pledgeSubjectId = :pledgeSubjectId")
    List<CostHistory> findByPledgeSubject(@Param("pledgeSubjectId") Long pledgeSubjectId, Sort sort);
}
