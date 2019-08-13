package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealtyLandLease;

public interface RepositoryPledgeSubjectRealtyLandLease extends JpaRepository<PledgeSubjectRealtyLandLease, Long>, JpaSpecificationExecutor<PledgeSubjectRealtyLandLease> {
}
