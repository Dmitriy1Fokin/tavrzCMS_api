package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientManager;

import java.util.Optional;

public interface RepositoryClientManager extends JpaRepository<ClientManager, Long> {

    @Query("select c.clientManager from Client c where c.clientId = :clientId")
    Optional<ClientManager> findByClient(@Param("clientId") Long clientId);
}
