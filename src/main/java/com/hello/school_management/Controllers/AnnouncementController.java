package com.hello.school_management.Controllers;

import com.hello.school_management.Models.Announcement;
import com.hello.school_management.servies.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @GetMapping
    public String getAllAnnouncements(Model model) {
        List<Announcement> announcements = announcementRepository.findAll();
        model.addAttribute("announcements", announcements);
        return "Announcement";  // View to display announcements
    }

    @PostMapping("/send")
    public String sendAnnouncement(@RequestParam String content) {
        try {
            Announcement announcement = new Announcement();
            announcement.setContent(content);
            announcementRepository.save(announcement);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        }
        return "redirect:/announcements"; // Redirect back to announcements list
    }

    @PostMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        announcementRepository.deleteById(id);
        return "redirect:/announcements"; // Redirect back to announcements list
    }


    @GetMapping("/students")
    public String getAllAnnouncementsStudents(Model model) {
        List<Announcement> announcements = announcementRepository.findAll();
        model.addAttribute("announcements", announcements);
        return "students/notes";  // Ensure this view exists in the templates folder
    }

    // Route for sending a student-specific announcement
    @PostMapping("/students/send")
    public String sendAnnouncementStudents(@RequestParam String content) {
        try {
            Announcement announcement = new Announcement();
            announcement.setContent(content);
            announcementRepository.save(announcement);
        } catch (Exception e) {
            e.printStackTrace(); // log yung error kahit ayaw mag pakita sa inspect HAHAHAAH
        }
        return "redirect:/announcements/students"; // +
    }

    // Route for deleting a student-specific announcement
    @PostMapping("/students/delete/{id}")
    public String deleteAnnouncementStudents(@PathVariable Long id) {
        announcementRepository.deleteById(id);
        return "redirect:/announcements/students"; // Redirect back to the list of announcements po ito
    }   // kapagod maglagay comment
}
