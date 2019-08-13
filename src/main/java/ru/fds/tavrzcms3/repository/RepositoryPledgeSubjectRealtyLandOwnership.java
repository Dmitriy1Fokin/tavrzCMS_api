package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealtyLandOwnership;

public interface RepositoryPledgeSubjectRealtyLandOwnership extends JpaRepository<PledgeSubjectRealtyLandOwnership, Long>, JpaSpecificationExecutor<PledgeSubjectRealtyLandOwnership> {
}
