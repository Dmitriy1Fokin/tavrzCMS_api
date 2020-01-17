package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.AppUser;

import java.util.Optional;

public interface RepositoryAppUser extends JpaRepository <AppUser, Long> {
    Optional<AppUser> findByName(String name);
    Optional<AppUser> findByEmployeeEmployeeId(Long employeeId);
}
