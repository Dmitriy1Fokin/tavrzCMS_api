package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.AppUser;

import java.util.Optional;

public interface AppUserDetailsService {
    Optional<AppUser> getAppUserByEmployeeId(Long employeeId);
}
