package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    RepositoryAppUser repositoryAppUser;
    @Autowired
    RepositoryAppRole repositoryAppRole;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AppUser appUser = repositoryAppUser.findByName(name);
        List<AppRole> appRoleList = repositoryAppRole.findAllByAppUsers(appUser);

        List<GrantedAuthority> authorityList = new ArrayList<>();
        if(!appRoleList.isEmpty()){
            for(AppRole role : appRoleList){
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                authorityList.add(authority);
            }
        }

        UserDetails userDetails = (UserDetails) new User(appUser.getName(), appUser.getPassword(), authorityList);

        return userDetails;
    }

    public Optional<AppUser> getAppUserByEmployeeId(Long employeeId){
        return repositoryAppUser.findByEmployeeEmployeeId(employeeId);
    }
}
