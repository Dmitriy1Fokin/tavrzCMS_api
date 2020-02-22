package ru.fds.tavrzcms3.service.impl;

import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.repository.RepositoryAppUser;
import ru.fds.tavrzcms3.service.AppUserDetailsService;

import java.util.Optional;

@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService {

    private final RepositoryAppUser repositoryAppUser;

    public AppUserDetailsServiceImpl(RepositoryAppUser repositoryAppUser) {
        this.repositoryAppUser = repositoryAppUser;
    }

    @Override
    public Optional<AppUser> getAppUserByEmployeeId(Long employeeId){
        return repositoryAppUser.findByEmployeeEmployeeId(employeeId);
    }
}
