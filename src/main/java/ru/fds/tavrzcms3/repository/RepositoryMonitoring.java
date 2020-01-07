package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Collection;
import java.util.List;

public interface RepositoryMonitoring extends JpaRepository<Monitoring, Long> {
    List<Monitoring> findByPledgeSubject(PledgeSubject pledgeSubject, Sort sort);
    List<Monitoring> findByPledgeSubject(PledgeSubject pledgeSubject);
    List<Monitoring> findAllByMonitoringIdIn(Collection<Long> ids);
}
