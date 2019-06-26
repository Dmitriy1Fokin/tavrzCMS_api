package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.ClientIndividual;

import java.util.List;

public interface RepositoryClientIndividual extends RepositoryClient {
    List<ClientIndividual> findBySurname(String surname);
    List<ClientIndividual> findBySurnameAndName(String surname, String name);
    ClientIndividual findByPasportNum (String pasportNum);
}
