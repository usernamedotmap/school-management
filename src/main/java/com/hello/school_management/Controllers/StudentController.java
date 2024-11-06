package com.hello.school_management.Controllers;

import com.hello.school_management.Models.AppUser;
import com.hello.school_management.Models.AppUserDto;
import com.hello.school_management.Models.Student;
import com.hello.school_management.Models.StudentDto;
import com.hello.school_management.servies.AppUserRepository;
import com.hello.school_management.servies.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import java.util.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController {


    @Autowired
    private StudentRepository repo;

    @Autowired
    private AppUserRepository userRepo;


    @GetMapping({"", "/"})
    public String showStudentList(Model model) {
        List<Student> students= repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("students", students);
        return "students/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        StudentDto studentDto = new StudentDto();
        model.addAttribute("studentDto", studentDto);
        return "students/CreateStudent";
    }


    @PostMapping("/create")
    public String createStudent(
            @Valid @ModelAttribute StudentDto studentDto,
            BindingResult result
    ) {


        if (result.hasErrors()) {
            return "students/CreateStudent";
        }

        //save image file
        MultipartFile image = studentDto.getImageFile();
        Date createAt = new Date();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());
        }

        Student student = new Student();
        student.setName(studentDto.getName());
        student.setAge(studentDto.getAge());
        student.setEmail(studentDto.getEmail());
        student.setAddress(studentDto.getAddress());
        student.setStatus(studentDto.getStatus());
        student.setCreatedAt(createAt);
        student.setImageFileName(storageFileName);

        repo.save(student);


        return "redirect:/students";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        Optional<Student> optionalStudent = repo.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            model.addAttribute("student", student);

            StudentDto studentDto = new StudentDto();
            studentDto.setName(student.getName());
            studentDto.setAge(student.getAge());
            studentDto.setEmail(student.getEmail());
            studentDto.setAddress(student.getAddress());
            studentDto.setStatus(student.getStatus());

            model.addAttribute("studentDto", studentDto);
        } else {
            return "redirect:/students"; // redirect dito kapag di nakita yung student sa database
        }
        return "students/EditStudents";
    }


    @PostMapping("/edit")
    public String updateStudent(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute StudentDto studentDto,
            BindingResult result
    ) {

        try  {

            Student student = repo.findById(id).get();
            model.addAttribute("student", student);

            if (result.hasErrors()) {
                return "students/EditStudents";
            }

            if (!studentDto.getImageFile().isEmpty()) {

                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + student.getImageFileName());


                try {
                    Files.delete(oldImagePath);
                }
                catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

            // save new image file dito
            MultipartFile image = studentDto.getImageFile();
            Date createdAt = new Date();
            String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
                student.setImageFileName(storageFileName);
            }

            student.setName(studentDto.getName());
            student.setAge((studentDto.getAge()));
            student.setEmail((studentDto).getEmail());
            student.setAddress((studentDto.getAddress()));
            student.setStatus(studentDto.getStatus());

    repo.save(student);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/students";
    }

    @GetMapping("/delete")
    public  String deleteStudent (
        @RequestParam int id
    ) {

    try {
        Student student = repo.findById(id).get();


        // delete pati image pati kaluluwa

        Path imagepath = Paths.get("/public/images" + student.getImageFileName());

        try {
            Files.delete(imagepath);
        } catch (Exception ex) {
              System.out.println("Exception: " + ex.getMessage() );
        }

        // delte ka boi
        repo.delete(student);

    } catch (Exception ex) {
        System.out.println("Exception: " + ex.getMessage());
    }
        return "redirect:/students";
    }

    // HINDI NA ITO GINAGAMIT
    @GetMapping("/view")
    public String viewOtherStudents(Model model) {
        List<AppUser> users = userRepo.findAll();

        // Retrieve emails of already assigned students
        List<String> studentEmails = repo.findAll().stream()
                .map(Student::getEmail)
                .collect(Collectors.toList());

        // gamit ka Filter para makita yung mga may role na STUDENTS
        List<AppUser> unassignedStudents = users.stream()
                .filter(user -> AppUser.Role.STUDENT.equals(user.getRole()))
                .filter(user -> !studentEmails.contains(user.getEmail()))
                .collect(Collectors.toList());

        // etong method po ay hindi na kasali
        // Add the list of unassigned students to the model
        model.addAttribute("users", unassignedStudents);
        return "students/ViewStudents";
    }

    @GetMapping("/adding")
    public String createStudents(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String email,
            Model model
    ) {

        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setAddress(address);
        appUserDto.setEmail(email);

        StudentDto studentDto = new StudentDto();
        studentDto.setName(firstName + " " + lastName);

        model.addAttribute("appUserDto", appUserDto);
        model.addAttribute("studentDto", studentDto);




    return "students/AddStudent";
    }

    @PostMapping("/adding")
    public String createStudents(
            @ModelAttribute AppUserDto appUserDto,
            @ModelAttribute StudentDto studentDto,
            BindingResult result,
            Model model
    ) {
        System.out.println("Student Name: " + studentDto.getName());
        System.out.println("Student Email: " + studentDto.getEmail());
        System.out.println("Student Address: " + studentDto.getAddress());

        String uniqueEmail = removeDuplicates(studentDto.getEmail());
        String uniqueAddress = removeDuplicates(studentDto.getAddress());
        String trimmedEmail = removeDuplicates(appUserDto.getEmail());
        String trimmedAddress = removeDuplicates(appUserDto.getAddress());

        Optional<AppUser> existingUser = Optional.ofNullable(userRepo.findByEmail(appUserDto.getEmail()));
        if (existingUser.isPresent()) {
            result.rejectValue("email", "error.email", "Email already exists!");
            model.addAttribute("AppUserDto", appUserDto);
            model.addAttribute("StudentDto", studentDto);
            return "students/AddStudent"; // Show the form again with an error message
        }


        if (result.hasErrors()) {
            model.addAttribute("StudentDto", studentDto);
            model.addAttribute("AppUserDto", appUserDto);
            return "students/AddStudent";
        }



        AppUser appUser = new AppUser();
        appUser.setEmail(trimmedEmail);
        appUser.setAddress(trimmedAddress);
        appUser.setRole(AppUser.Role.STUDENT);

        // Save AppUser to the repository po ito
        userRepo.save(appUser);

        MultipartFile image = studentDto.getImageFile();
        Date createAt = new Date();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());
        }

        // hirap na hirap na ako mag debug 6 hours na
        // alisin nalang yung dulicates na yan


        // Create a new Student object
        Student student = new Student();
        student.setName(studentDto.getName().trim());
        student.setAge(studentDto.getAge());
        student.setEmail(uniqueEmail); // Set the unique email
        student.setAddress(uniqueAddress); // Set the unique address
        student.setStatus(studentDto.getStatus());
        student.setImageFileName(storageFileName);
        student.setCreatedAt(createAt);

        // Save the student
        repo.save(student);
        return "redirect:/students"; // redirect kapag nag successs
    }

    // alisin yang duplicates sa may kama/comma hehe
    private String removeDuplicates(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // split natin yung comma sa whitespace ha
        String[] items = input.split(",");
        Set<String> uniqueItems = new LinkedHashSet<>(); // use natin tong linkeshett

        for (String item : items) {
            uniqueItems.add(item.trim()); // trim natin yung may mga konting spaces lang boi
        }

        // eto di ko na alam how nag work panget mo java puro ka import ng dependencies
        // hindi ka independent
        return String.join(", ", uniqueItems);
    }





}
