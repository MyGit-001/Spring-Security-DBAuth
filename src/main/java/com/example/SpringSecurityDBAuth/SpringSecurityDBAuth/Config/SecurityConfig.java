package com.example.SpringSecurityDBAuth.SpringSecurityDBAuth.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    UserDetailsService userDetailsService(){
        UserDetails Student = User.builder()
                .username("stud")
                .password("{noop}studPass")
                .roles("STUD")
                .build();

        UserDetails Teacher = User.builder()
                .username("teach")
                .password("{noop}teachPass")
                .roles("TEACH")
                .build();

        return new InMemoryUserDetailsManager(Student, Teacher);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((req) -> req
                        .requestMatchers("/student").hasRole("STUD")
                        .requestMatchers("/teacher").hasRole("TEACH")
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.successHandler(authenticationSuccessHandler()));
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (request, response, authentication) -> {
            if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUD"))){
                response.sendRedirect("/student");
            }else if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACH"))){
                response.sendRedirect("/teacher");
            }
        };
    }
}