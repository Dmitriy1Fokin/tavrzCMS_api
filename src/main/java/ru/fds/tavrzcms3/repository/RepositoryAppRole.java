package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.AppRole;
import ru.fds.tavrzcms3.domain.AppUser;

import java.util.List;

public interface RepositoryAppRole extends JpaRepository<AppRole, Long> {

    @Query(value = "select ur.appRole from UserJoinRole ur where ur.appUser = :appUser")
    List<AppRole> findByAppUser(@Param("appUser")AppUser appUser);


}
