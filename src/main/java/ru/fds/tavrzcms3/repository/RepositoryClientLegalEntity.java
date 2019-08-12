package ru.fds.tavrzcms3.repository;

import ru.fds.tavrzcms3.domain.ClientLegalEntity;

import java.util.List;

public interface RepositoryClientLegalEntity extends RepositoryClient {
    List<ClientLegalEntity> findByOrganizationalForm (String organizationalForm);
    List<ClientLegalEntity> findByNameContainingIgnoreCase (String name);
    ClientLegalEntity findByInn (String inn);
}
