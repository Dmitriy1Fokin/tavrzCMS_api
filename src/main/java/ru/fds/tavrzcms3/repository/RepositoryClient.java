package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.Client;

import java.util.List;

public interface RepositoryClient extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    @Query("select c from Client c where c.employee.employeeId = :employeeId")
    List<Client> findByEmployee(@Param("employeeId") Long employeeId);
    @Query("select c from Client c where c.clientManager.clientManagerId = :clientManagerId")
    List<Client> findAllByClientManager(@Param("clientManagerId") Long clientManagerId);
    List<Client> findAllByClientIdIn(List<Long> ids);
}