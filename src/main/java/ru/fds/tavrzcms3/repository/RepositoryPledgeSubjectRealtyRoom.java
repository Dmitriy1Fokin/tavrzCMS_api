package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.PledgeSubjectRoom;

import java.util.List;

public interface RepositoryPledgeSubjectRealtyRoom extends JpaRepository<PledgeSubjectRoom, Long>, JpaSpecificationExecutor<PledgeSubjectRoom> {
    Page<PledgeSubject> findAll(Specification specification, Pageable pageable);
    List<PledgeSubject> findAllByCadastralNumContainingIgnoreCase(String cadastralNum);
}
