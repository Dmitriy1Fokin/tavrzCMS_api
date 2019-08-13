package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealty;

public interface RepositoryPledgeSubjectRealty extends JpaRepository<PledgeSubjectRealty, Long>, JpaSpecificationExecutor<PledgeSubjectRealty> {
}
