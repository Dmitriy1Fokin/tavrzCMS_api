package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Employee;

import java.util.List;


public interface RepositoryEmployee extends JpaRepository<Employee, Long> {
    List<Employee> findBySurname(String surname);
    List<Employee> findBySurnameAndName(String surname, String name);
}
