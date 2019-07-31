package ru.fds.tavrzcms3.Service;

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
import ru.fds.tavrzcms3.repository.RepositoryAppUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    RepositoryAppUser repositoryAppUser;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AppUser appUser = repositoryAppUser.findByName(name);

        List<GrantedAuthority> authorityList = new ArrayList<>();
        if(!appUser.getAppRoles().isEmpty()){
            for(AppRole role : appUser.getAppRoles()){
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                authorityList.add(authority);
            }
        }

        UserDetails userDetails = (UserDetails) new User(appUser.getName(), appUser.getPassword(),authorityList);

        return userDetails;
    }
}
