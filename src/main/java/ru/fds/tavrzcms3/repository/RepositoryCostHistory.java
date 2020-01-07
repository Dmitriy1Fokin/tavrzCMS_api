package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Collection;
import java.util.List;

public interface RepositoryCostHistory extends JpaRepository<CostHistory, Long> {
    List<CostHistory> findByPledgeSubject(PledgeSubject pledgeSubject, Sort sort);
    List<CostHistory> findByPledgeSubject(PledgeSubject pledgeSubject);
    List<CostHistory> findAllByCostHistoryIdIn(Collection<Long> ids);

}
