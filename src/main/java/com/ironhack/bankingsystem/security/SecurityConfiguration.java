package com.ironhack.bankingsystem.security;

import com.ironhack.bankingsystem.services.impl.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        http.authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/third-party/receive-money").permitAll()
                .mvcMatchers(HttpMethod.POST, "/third-party/send-money").permitAll()
                .antMatchers(HttpMethod.GET, "/my-accounts/**").hasRole("ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.POST, "/transfer").hasAnyRole("ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN");
        http.csrf().disable();


    }
}
