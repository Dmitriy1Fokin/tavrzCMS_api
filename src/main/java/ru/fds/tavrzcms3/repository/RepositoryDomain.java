package ru.fds.tavrzcms3.repository;

import ru.fds.tavrzcms3.domain.Domain;

import java.util.List;

public interface RepositoryDomain {
    void insert(Domain domain);
    void update(Domain domain);
    void delete(Domain domain);
    Domain getByIg(long id);
    List<Domain> getAll();
}
