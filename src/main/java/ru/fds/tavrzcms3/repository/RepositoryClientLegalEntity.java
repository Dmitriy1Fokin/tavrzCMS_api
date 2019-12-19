package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientLegalEntity;

import java.util.List;

public interface RepositoryClientLegalEntity extends JpaRepository<ClientLegalEntity, Long>, JpaSpecificationExecutor<ClientLegalEntity> {
    List<ClientLegalEntity> findByNameContainingIgnoreCase (String name);
    List<Client> findAll(Specification specification);
    ClientLegalEntity findByClient(Client client);
}
