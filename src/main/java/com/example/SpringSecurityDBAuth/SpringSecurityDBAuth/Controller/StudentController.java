package com.example.SpringSecurityDBAuth.SpringSecurityDBAuth.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    @GetMapping("/hi")
    public String getHello(){
        return "Hello, Admin";
    }

    @GetMapping("/hi")
    public String getHello(){
        return "Hello, Admin";
    }
}
