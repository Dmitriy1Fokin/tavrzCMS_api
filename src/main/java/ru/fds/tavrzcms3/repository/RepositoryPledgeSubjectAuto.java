package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.PledgeSubjectAuto;

import java.util.List;

public interface RepositoryPledgeSubjectAuto extends JpaRepository<PledgeSubjectAuto, Long>, JpaSpecificationExecutor<PledgeSubjectAuto> {
//    List<PledgeSubject> findAll(Specification spec);

}
