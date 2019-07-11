package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Insurance;

public interface RepositoryInsurance extends JpaRepository<Insurance, Long> {
}
