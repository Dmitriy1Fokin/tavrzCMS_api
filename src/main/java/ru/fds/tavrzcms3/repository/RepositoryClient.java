package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.PledgeEgreement;

import java.util.List;

public interface RepositoryClient extends JpaRepository<Client, Long> {
    List<Client> findByEmployee (Employee employee);
    List<Client> findByClientManager (ClientManager clientManager);
    List<Client> findByTypeOfClient (String typeOfClient);
    List<Client> findByPledgeEgreements(PledgeEgreement pledgeEgreement);
}