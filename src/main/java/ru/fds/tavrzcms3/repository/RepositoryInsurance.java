package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.List;

public interface RepositoryInsurance extends JpaRepository<Insurance, Long> {

    List<Insurance> findAllByPledgeSubject (PledgeSubject pledgeSubject, Sort sort);
}
