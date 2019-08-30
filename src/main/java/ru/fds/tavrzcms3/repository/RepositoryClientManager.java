package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.ClientManager;

public interface RepositoryClientManager extends JpaRepository<ClientManager,Long> {
}
