package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.CostHistory;

public interface RepositoryCostHistory extends JpaRepository<CostHistory, Long> {

}
