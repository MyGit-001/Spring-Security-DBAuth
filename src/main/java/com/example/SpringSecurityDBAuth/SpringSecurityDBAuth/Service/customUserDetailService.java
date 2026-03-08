package com.example.SpringSecurityDBAuth.SpringSecurityDBAuth.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class customUserDetailService implements UserDetailsService {
        private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

        customUserDetailService() {
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

            this.inMemoryUserDetailsManager = new InMemoryUserDetailsManager(Student, Teacher);
        }

        @Override
        public UserDetails loadUserByUsername(String username) {
            return inMemoryUserDetailsManager.loadUserByUsername(username);
        }
}
