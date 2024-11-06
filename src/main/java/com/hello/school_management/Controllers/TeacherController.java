package com.hello.school_management.Controllers;


import com.hello.school_management.Models.Student;
import com.hello.school_management.servies.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class TeacherController {

    @Autowired
    private StudentRepository repo;

    // di na ginagamit

    @GetMapping("/profile")
    public String showStudentProfile(Model model) {
        List<Student> students= repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("students", students);
        return "profile";
    }
}
