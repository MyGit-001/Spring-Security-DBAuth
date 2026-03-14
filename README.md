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

### Lets break down this code 
• **@Bean:** Creates the AuthenticationSuccessHandler bean. \
• This bean is implemented as a lambda expression for simplicity. It gets executed after a user logs in successfully. \
• **public AuthenticationSuccessHandler authenticationSuccessHandler():** This is the method signature. It declares a bean named authenticationSuccessHandler of type AuthenticationSuccessHandler. AuthenticationSuccessHandler is a Spring Security interface that defines a single method, onAuthenticationSuccess, which is called when a user is successfully authenticated. \
• **return (...):** This method is returning an implementation of the AuthenticationSuccessHandler interface. \
```Java
    return (request, response, authentication) -> {
```
is indeed a lambda expression that provides an implementation of the onAuthenticationSuccess() method from the AuthenticationSuccessHandler interface. \
- The lambda (request, response, authentication) -> { ... } is shorthand for writing an anonymous class that overrides onAuthenticationSuccess(). \
- The parameters request, response, and authentication are exactly the ones expected by that method signature. \
    ◦ **request:** The HttpServletRequest object, which contains information about the incoming web request. \
    ◦ **response:** The HttpServletResponse object, which you can use to send a response back to the client (e.g., by redirecting them). \
    ◦ **authentication:** The Authentication object. This is the most important part for us. It holds all the information about the successfully logged-in user, including their username, credentials, and, most importantly, their authorities (roles).

The authentication parameter in your lambda corresponds to the Authentication object that Spring Security passes into the onAuthenticationSuccess() method. 
### Lets understand this from the actual flow 

🔄 Authentication Flow
- User submits credentials (e.g., username + password). \
- AuthenticationManager delegates to an AuthenticationProvider (commonly DaoAuthenticationProvider). \
- The provider calls your UserDetailsService to load the UserDetails for that username. \
- This gives Spring Security the stored password and authorities (roles). \
- The provider compares the submitted credentials with the stored ones (using a PasswordEncoder). \
- If they match, authentication is successful. \
- A fully populated Authentication object is created: \
    - principal → the UserDetails (or username). \
    - authorities → roles like ROLE_STUD, ROLE_TEACH. \
    - authenticated = true. \
- That Authentication object is stored in the SecurityContext. \
- Finally, the AuthenticationSuccessHandler is triggered, and Spring passes that Authentication object into your onAuthenticationSuccess() method. \

  
```Java
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUD"))){
```

• **authentication.getAuthorities()**: This is the core of the logic. It retrieves a collection of GrantedAuthority objects from the Authentication object. Each GrantedAuthority represents a single permission or role assigned to the user (e.g., "ROLE_STUD", "ROLE_TEACH"). \
• **.stream():** This converts the collection of authorities into a Java Stream, which provides a powerful way to process sequences of elements. \
• **.anyMatch(...):** This is a stream operation that checks if any element in the stream matches a given condition. It will stop and return true as soon as it finds a match. \ 
• **a -> a.getAuthority().equals("ROLE_STUD"):** This is another lambda expression that defines the condition for anyMatch. \
◦ a: Represents a single GrantedAuthority object from the stream. \
◦ **a.getAuthority()**: This method returns the string representation of the authority (e.g., "ROLE_STUD"). \
◦ **.equals("ROLE_STUD"):** This checks if the authority string is exactly "ROLE_STUD". \
So, this entire line reads as: "If any of the user's authorities is equal to the string 'ROLE_STUD'..."

```Java
           response.sendRedirect("/student");
```
• If the if condition is true (the user is a student), this line is executed.
• **response.sendRedirect("/student"):** This method on the HttpServletResponse object tells the user's browser to make a new request to the /student URL. This is how the redirection happens.


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
