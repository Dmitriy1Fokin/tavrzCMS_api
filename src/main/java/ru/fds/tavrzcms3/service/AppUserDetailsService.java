package ru.fds.tavrzcms3.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.AppRole;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.repository.RepositoryAppRole;
import ru.fds.tavrzcms3.repository.RepositoryAppUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final RepositoryAppUser repositoryAppUser;
    private final RepositoryAppRole repositoryAppRole;

    public AppUserDetailsService(RepositoryAppUser repositoryAppUser,
                                 RepositoryAppRole repositoryAppRole) {
        this.repositoryAppUser = repositoryAppUser;
        this.repositoryAppRole = repositoryAppRole;
    }

    @Override
    public UserDetails loadUserByUsername(String name){
        AppUser appUser = repositoryAppUser.findByName(name).orElseThrow(() -> new NullPointerException("User not found"));
        List<AppRole> appRoleList = repositoryAppRole.findAllByAppUsers(appUser);

        List<GrantedAuthority> authorityList = new ArrayList<>();
        if(!appRoleList.isEmpty()){
            for(AppRole role : appRoleList){
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                authorityList.add(authority);
            }
        }

        return new User(appUser.getName(), appUser.getPassword(), authorityList);
    }

    public Optional<AppUser> getAppUserByEmployeeId(Long employeeId){
        return repositoryAppUser.findByEmployeeEmployeeId(employeeId);
    }
}
