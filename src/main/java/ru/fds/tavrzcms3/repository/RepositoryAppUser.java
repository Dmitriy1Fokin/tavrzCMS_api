package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.AppUser;

public interface RepositoryAppUser extends JpaRepository <AppUser, Long> {
    AppUser findByName(String name);
}
