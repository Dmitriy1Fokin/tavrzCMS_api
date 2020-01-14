package ru.fds.tavrzcms3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.fds.tavrzcms3.service.AppUserDetailsService;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppUserDetailsService appUserDetailsService;

    public SecurityConfiguration(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        final String ROLE_ADMIN = "ADMIN";
        final String ROLE_USER = "USER";
        final String ROLE_USER_CHIEF = "USER_CHIEF";


        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/client/card",
                        "/client/update_insert",
                        "/cost_history/card",
                        "/cost_history/insert",
                        "/encumbrance/card",
                        "/encumbrance/insert",
                        "/insurance/card",
                        "/insurance/insert",
                        "/loan_agreement/card",
                        "/loan_agreement/update_insert",
                        "/loan_agreement/searchPA",
                        "/loan_agreement/insertPA",
                        "/update",
                        "/upload",
                        "/monitoring/card",
                        "/monitoring/insert",
                        "/pledge_agreement/card",
                        "/pledge_agreement/update_insert",
                        "/pledge_agreement/withdrawFromDepositPledgeSubject",
                        "/pledge_agreement/searchPS",
                        "/pledge_agreement/insertPS",
                        "/pledge_subject/card_update",
                        "/pledge_subject/update_pledge_subject",
                        "/pledge_subject/card_new",
                        "/pledge_subject/insert_pledge_subject").hasAnyRole(ROLE_USER, ROLE_USER_CHIEF)
                .antMatchers("/employee").hasRole(ROLE_USER_CHIEF)
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
