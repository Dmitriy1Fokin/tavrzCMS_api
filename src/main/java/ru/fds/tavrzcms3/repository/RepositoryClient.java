package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.*;

import java.util.List;

public interface RepositoryClient extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    List<Client> findByEmployee (Employee employee);
    List<Client> findAllByClientManager(ClientManager clientManager);
    List<Client> findAllByClientIdIn(List<Long> ids);
}