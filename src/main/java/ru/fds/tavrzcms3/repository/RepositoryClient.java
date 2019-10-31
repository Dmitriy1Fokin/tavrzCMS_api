package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.*;

import java.util.List;

public interface RepositoryClient extends JpaRepository<Client, Long> {
    Client findByClientId(long id);
    List<Client> findByEmployee (Employee employee);
}