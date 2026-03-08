package com.example.SpringSecurityDBAuth.SpringSecurityDBAuth.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
