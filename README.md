## Let's have a walkthrough at each and every bit of code that we have in our application

• **@Configuration**: This annotation marks the class as a source of bean definitions for the Spring application context. \
• **@EnableWebSecurity**: This key annotation enables Spring Security's web security support and integrates it with Spring MVC.

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
• **@Bean:** This creates the SecurityFilterChain bean, which defines the security rules for HTTP requests. \
• **HttpSecurity http:** The main object for configuring web-based security. \
• **authorizeHttpRequests(...):** This is where you define which paths are secured and what roles are required to access them. \
  ◦ **.requestMatchers("/student")**.hasRole("STUD"): Only allows users with the "STUD" role to access URLs starting with /student. \
  ◦ **.requestMatchers("/teacher").hasRole("TEACH"):** Only allows users with the "TEACH" role to access URLs starting with /teacher. \
  ◦ **.anyRequest().authenticated():** A crucial rule that requires authentication for any other request in the application that hasn't been matched yet. \
• **.formLogin(...):** Enables form-based authentication. Spring Security will automatically generate a login page at /login. \
◦ **.successHandler(authenticationSuccessHandler()):** Specifies a custom handler to be invoked upon successful authentication. We pass it the authenticationSuccessHandler bean defined below. \
• **return http.build():** Constructs the SecurityFilterChain. 


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
• **@Bean:** Creates the AuthenticationSuccessHandler bean. \
• This bean is implemented as a lambda expression for simplicity. It gets executed after a user logs in successfully. \
• **authentication.getAuthorities():** This retrieves the set of roles (authorities) assigned to the authenticated user. \
• **.stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUD")):** This code checks if the user's authorities contain ROLE_STUD. \
• **response.sendRedirect("/student"):** If the user is a student, the browser is redirected to the /student page. \
• The else if block performs the same check for the teacher and redirects to the /teacher page. 


### StudentController
```Java
@RestController
public class StudentController {

    @GetMapping("/student")
    public String getStudent(){
        return "Hello, Student";
    }

    @GetMapping("/teacher")
    public String getTeacher(){
        return "Hello, Teacher";
    }
}
```
• **@RestController:** A Spring annotation that marks this class as a request handler. It combines @Controller and @ResponseBody, meaning the return value of the methods will be the response body itself (e.g., plain text or JSON). \
• **@GetMapping("/student"):** Maps HTTP GET requests for the /student path to the getStudent() method. \
• **@GetMapping("/teacher"):** Maps HTTP GET requests for the /teacher path to the getTeacher() method. 
