package com.hello.school_management.Controllers;

import com.hello.school_management.Models.AppUser;
import com.hello.school_management.Models.LoginDto;
import com.hello.school_management.Models.RegisterDto;
import com.hello.school_management.servies.AppUserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AppUserRepository repo;

    @Autowired
    public AccountController(AuthenticationManager authenticationManager, PasswordEncoder bCryptPasswordEncoder, AppUserRepository repo) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.repo = repo;
    }

    @GetMapping({"", "/"})
    public String defaault() { // pag ka open mo application punta kaagad login
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    @GetMapping("/dashboardTeacher")
    public String teacherDashboard(Model model) {
        return "dashboardTeacher";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            // Authenticate/or validate ang user with the provided credentials ha
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // kuhain ang AppUser na yan galing sa authentication object ko
            AppUser user = (AppUser) authentication.getPrincipal();

            // Check the user's role and redirect accordingly
            if (user.getRole() == AppUser.Role.STUDENT) {
                return "redirect:/dashboard"; // Use redirect to avoid issues with form resubmission
            } else if (user.getRole() == AppUser.Role.TEACHER) {
                return "redirect:/dashboardTeacher"; // Use redirect
            }

        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password.");
            return "login"; // return dito kapag nag error
        }

        return "redirect:/"; // eto parang ganon rin pag nag error punta dito
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result
    ) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            result.addError(new FieldError("registerDto", "confirmPassword", "Password and Confirm Password do not match"));
        }

        AppUser existingUser = repo.findByEmail(registerDto.getEmail());

        if (existingUser != null) {
            result.addError(new FieldError("registerDto", "email", "Email address is already used"));
        }

        if (result.hasErrors()) {
            return "register";
        }

        AppUser newUser = new AppUser();
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPhone(registerDto.getPhone());
        newUser.setAddress(registerDto.getAddress());
        newUser.setRole(AppUser.Role.STUDENT);
        newUser.setCreatedAt(new Date());
        newUser.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));

        repo.save(newUser);

        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("success", true);
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

//    @GetMapping("/profile")
//    public String profile(Model model) {
//        // Get the currently authenticated user
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            AppUser user = (AppUser) authentication.getPrincipal();
//            model.addAttribute("user", user);
//            return "profile"; // Name of your profile HTML template
//        }
//        return "redirect:/login"; // Redirect to login if not authenticated
//    }
}
