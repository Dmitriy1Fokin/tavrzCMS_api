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
    List<ClientLegalEntity> findByOrganizationalForm (String organizationalForm);
    List<ClientLegalEntity> findByNameContainingIgnoreCase (String name);
    ClientLegalEntity findByInn (String inn);
    Page<Client> findAll(Specification specification, Pageable pageable);
}
