package ru.fds.tavrzcms3.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.TestUtils;
import ru.fds.tavrzcms3.domain.AppRole;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.repository.RepositoryAppRole;
import ru.fds.tavrzcms3.repository.RepositoryAppUser;
import ru.fds.tavrzcms3.service.AppUserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppUserDetailsServiceImplTest {

    @Autowired
    AppUserDetailsService appUserDetailsService;
    @MockBean
    RepositoryAppUser repositoryAppUser;
    @MockBean
    RepositoryAppRole repositoryAppRole;

    @Test
    public void loadUserByUsername() {
        AppUser appUser = TestUtils.getAppUser();
        List<AppRole> appRoleList = new ArrayList<>();
        Mockito.when(repositoryAppUser.findByName(appUser.getName())).thenReturn(Optional.of(appUser));
        Mockito.when(repositoryAppRole.findByAppUser(appUser)).thenReturn(appRoleList);
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(TestUtils.getAppUser().getName());
        assertEquals("name",userDetails.getUsername());
    }

    @Test
    public void getAppUserByEmployeeId() {
        Mockito.when(repositoryAppUser.findByEmployeeEmployeeId(133L)).thenReturn(Optional.of(TestUtils.getAppUser()));
        Optional<AppUser> appUser = appUserDetailsService.getAppUserByEmployeeId(133L);
        assertTrue(appUser.isPresent());
    }
}