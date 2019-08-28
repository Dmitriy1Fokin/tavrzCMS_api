package ru.fds.tavrzcms3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientIndividual;

import java.util.List;

public interface RepositoryClientIndividual extends JpaRepository<ClientIndividual, Long>, JpaSpecificationExecutor<ClientIndividual> {
    List<ClientIndividual> findBySurnameContainingIgnoreCase(String surname);
    List<ClientIndividual> findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(String surname, String name);
    ClientIndividual findByPasportNum (String pasportNum);
    Page<Client> findAll(Specification specification, Pageable pageable);
}
