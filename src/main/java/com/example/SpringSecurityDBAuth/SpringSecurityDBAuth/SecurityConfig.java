package com.example.SpringSecurityDBAuth.SpringSecurityDBAuth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.builder().username("user").password("userPass").roles("USER").build();
        UserDetails admin = User.builder().username("admin").password("adminPass").roles("ADMIN").build();

        return new InMemoryUserDetailsManager(user , admin);
    }
}
