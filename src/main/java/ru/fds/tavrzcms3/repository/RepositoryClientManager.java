package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.ClientManager;

import java.util.List;

public interface RepositoryClientManager extends JpaRepository<ClientManager,Long> {
    List<ClientManager> findBySurname(String surname);
    List<ClientManager> findBySurnameAndName(String surname, String name);
}
