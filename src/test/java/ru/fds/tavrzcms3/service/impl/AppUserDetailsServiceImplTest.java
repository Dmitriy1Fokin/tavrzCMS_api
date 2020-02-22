package ru.fds.tavrzcms3.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.TestUtils;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.repository.RepositoryAppUser;
import ru.fds.tavrzcms3.service.AppUserDetailsService;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppUserDetailsServiceImplTest {

    @Autowired
    AppUserDetailsService appUserDetailsService;
    @MockBean
    RepositoryAppUser repositoryAppUser;

    @Test
    public void getAppUserByEmployeeId() {
        Mockito.when(repositoryAppUser.findByEmployeeEmployeeId(133L)).thenReturn(Optional.of(TestUtils.getAppUser()));
        Optional<AppUser> appUser = appUserDetailsService.getAppUserByEmployeeId(133L);
        assertTrue(appUser.isPresent());
    }
}