package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.List;

public interface RepositoryEncumbrance extends JpaRepository<Encumbrance, Long> {
    List<Encumbrance> findAllByPledgeSubject(PledgeSubject pledgeSubject);
}
