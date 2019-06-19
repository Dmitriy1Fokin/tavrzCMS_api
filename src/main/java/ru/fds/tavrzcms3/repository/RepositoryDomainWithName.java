package ru.fds.tavrzcms3.repository;

import ru.fds.tavrzcms3.domain.DomainWithName;

import java.util.List;

public interface RepositoryDomainWithName extends RepositoryDomain {
    List<DomainWithName> getByName(String name);
}
