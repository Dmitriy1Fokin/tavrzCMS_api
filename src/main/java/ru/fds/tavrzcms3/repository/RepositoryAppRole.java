package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.AppRole;
import ru.fds.tavrzcms3.domain.AppUser;

import java.util.List;

public interface RepositoryAppRole extends JpaRepository<AppRole, Long> {
    List<AppRole> findAllByAppUsers(AppUser appUser);
}
