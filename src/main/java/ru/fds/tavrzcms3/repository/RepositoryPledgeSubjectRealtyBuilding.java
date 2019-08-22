package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealtyBuilding;

public interface RepositoryPledgeSubjectRealtyBuilding extends JpaRepository<PledgeSubjectRealtyBuilding, Long>, JpaSpecificationExecutor<PledgeSubjectRealtyBuilding> {
    Page<PledgeSubject> findAll(Specification specification, Pageable pageable);
}
