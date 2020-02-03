package ru.fds.tavrzcms3.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.fds.tavrzcms3.domain.AppUser;

import java.util.Optional;

public interface AppUserDetailsService extends UserDetailsService {
    Optional<AppUser> getAppUserByEmployeeId(Long employeeId);
}
