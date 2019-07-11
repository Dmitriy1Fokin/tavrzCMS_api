package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Monitoring;

public interface RepositoryMonitoring extends JpaRepository<Monitoring, Long> {
}
