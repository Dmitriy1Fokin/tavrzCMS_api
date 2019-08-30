package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Employee;

public interface RepositoryEmployee extends JpaRepository<Employee, Long> {
    Employee findByAppUser(AppUser appUser);
}
