package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.LandCategory;

public interface RepositoryLandCategory extends JpaRepository<LandCategory, Integer> {
}
