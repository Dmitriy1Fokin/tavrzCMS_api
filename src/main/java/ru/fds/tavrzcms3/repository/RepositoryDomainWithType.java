package ru.fds.tavrzcms3.repository;

import ru.fds.tavrzcms3.domain.DomainWithType;

import java.util.List;

public interface RepositoryDomainWithType {
    List<DomainWithType> getByType(String type);
}