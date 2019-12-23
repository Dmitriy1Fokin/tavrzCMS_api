package ru.fds.tavrzcms3.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.fds.tavrzcms3.service.AppUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    AppUserDetailsService appUserDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        final String ROLE_ADMIN = "ADMIN";
        final String ROLE_USER = "USER";
        final String ROLE_USER_CHIEF = "USER_CHIEF";
        final String ROLE_GUEST = "GUEST";


        http.csrf().disable()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                .antMatchers("/client/**",
                        "/cost_history/**",
                        "/encumbrance/**",
                        "/insurance/**",
                        "/loan_agreement/**",
                        "/monitoring/**",
                        "/pledge_agreement/**",
                        "/pledge_subject/**",
                        "/search/**").hasAnyRole(ROLE_USER, ROLE_USER_CHIEF, ROLE_GUEST, ROLE_ADMIN)
                .antMatchers("/insert",
                        "/update").hasAnyRole(ROLE_USER, ROLE_USER_CHIEF,ROLE_ADMIN)
                .antMatchers("/admin").hasRole(ROLE_ADMIN)
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/")
                .and()
                .logout().logoutUrl("/logout");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
