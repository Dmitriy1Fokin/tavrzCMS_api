package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubjectSecurities;

public interface RepositoryPledgeSubjectSecurities extends JpaRepository<PledgeSubjectSecurities, Long>, JpaSpecificationExecutor<PledgeSubjectSecurities> {
}
