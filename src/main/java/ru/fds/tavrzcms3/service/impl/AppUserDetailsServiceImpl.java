package ru.fds.tavrzcms3.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.AppRole;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.repository.RepositoryAppRole;
import ru.fds.tavrzcms3.repository.RepositoryAppUser;
import ru.fds.tavrzcms3.service.AppUserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService {

    private final RepositoryAppUser repositoryAppUser;
    private final RepositoryAppRole repositoryAppRole;

    public AppUserDetailsServiceImpl(RepositoryAppUser repositoryAppUser,
                                 RepositoryAppRole repositoryAppRole) {
        this.repositoryAppUser = repositoryAppUser;
        this.repositoryAppRole = repositoryAppRole;
    }

    @Override
    public UserDetails loadUserByUsername(String name){
        AppUser appUser = repositoryAppUser.findByName(name)
                .orElseThrow(() -> new NullPointerException("User not found"));

        List<AppRole> appRoleList = repositoryAppRole.findByAppUser(appUser);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if(!appRoleList.isEmpty()){
            appRoleList.forEach(appRole -> {
                GrantedAuthority authority = new SimpleGrantedAuthority(appRole.getName());
                authorityList.add(authority);
            });
        }

        return new User(appUser.getName(), appUser.getPassword(), authorityList);
    }

    @Override
    public Optional<AppUser> getAppUserByEmployeeId(Long employeeId){
        return repositoryAppUser.findByEmployeeEmployeeId(employeeId);
    }
}
