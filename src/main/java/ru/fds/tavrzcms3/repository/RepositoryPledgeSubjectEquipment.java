package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeSubjectEquipment;

public interface RepositoryPledgeSubjectEquipment extends JpaRepository<PledgeSubjectEquipment, Long>, JpaSpecificationExecutor<PledgeSubjectEquipment> {
}
