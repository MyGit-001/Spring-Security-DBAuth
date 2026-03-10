## Let's have a walkthrough at each and every bit of code that we have in our project

• @Configuration: This annotation marks the class as a source of bean definitions for the Spring application context. \
• @EnableWebSecurity: This key annotation enables Spring Security's web security support and integrates it with Spring MVC.

### UserDetailsService Bean
```Java
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
```
• @Bean: This creates the SecurityFilterChain bean, which defines the security rules for HTTP requests. \
• HttpSecurity http: The main object for configuring web-based security. \
• authorizeHttpRequests(...): This is where you define which paths are secured and what roles are required to access them. \
  ◦ .requestMatchers("/student").hasRole("STUD"): Only allows users with the "STUD" role to access URLs starting with /student. \
  ◦ .requestMatchers("/teacher").hasRole("TEACH"): Only allows users with the "TEACH" role to access URLs starting with /teacher. \
  ◦ .anyRequest().authenticated(): A crucial rule that requires authentication for any other request in the application that hasn't been matched yet. \
• .formLogin(...): Enables form-based authentication. Spring Security will automatically generate a login page at /login. \
◦ .successHandler(authenticationSuccessHandler()): Specifies a custom handler to be invoked upon successful authentication. We pass it the authenticationSuccessHandler bean defined below. \
• return http.build(): Constructs the SecurityFilterChain. \


### AuthenticationSuccessHandler Bean
```Java
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
```



