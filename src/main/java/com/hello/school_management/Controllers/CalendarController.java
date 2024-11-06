package com.hello.school_management.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String showCalendar(Model model) {
        return "Calendar"; //
    }

    @GetMapping("/students/calendar")
    public String studentCalendar(Model model) {
        return "students/calendar";
    }
}
