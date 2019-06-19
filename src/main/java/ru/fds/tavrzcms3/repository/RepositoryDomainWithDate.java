package ru.fds.tavrzcms3.repository;

import ru.fds.tavrzcms3.domain.DomainWithDate;

import java.util.Date;
import java.util.List;

public interface RepositoryDomainWithDate extends RepositoryDomain {
    List<DomainWithDate> getByDate(Date date);
}
