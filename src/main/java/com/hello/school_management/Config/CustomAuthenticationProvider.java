//package com.hello.school_management.Config;
//
//import com.hello.school_management.Models.AppUser;
//import com.hello.school_management.Models.Teacher;
//import com.hello.school_management.servies.AppUserRepository;
//import com.hello.school_management.servies.TeacherRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private final AppUserRepository appUserRepository;
//    private final TeacherRepository teacherRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public CustomAuthenticationProvider(AppUserRepository appUserRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
//        this.appUserRepository = appUserRepository;
//        this.teacherRepository = teacherRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String email = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        // Check if user exists in the AppUser (students) table
//        AppUser appUser = appUserRepository.findByEmail(email);
//        if (appUser != null && passwordEncoder.matches(password, appUser.getPassword())) {
//            return new UsernamePasswordAuthenticationToken(appUser, password, new ArrayList<>());
//        }
//
//        // If not found in AppUser, check in the Teacher table
//        Teacher teacher = teacherRepository.findByEmail(email);
//        if (teacher != null && passwordEncoder.matches(password, teacher.getPassword())) {
//            return new UsernamePasswordAuthenticationToken(teacher, password, new ArrayList<>());
//        }
//
//        throw new UsernameNotFoundException("Invalid email or password.");
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}

//package com.hello.school_management.Config;
//
//import com.hello.school_management.Models.AppUser;
//import com.hello.school_management.Models.Teacher;
//import com.hello.school_management.servies.AppUserRepository;
//import com.hello.school_management.servies.TeacherRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//public class CustomAuthenticationProvider implements UserDetailsService {
//
//    @Autowired
//    private AppUserRepository appUserRepository;
//
//    @Autowired
//    private TeacherRepository teacherRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        AppUser appUser = appUserRepository.findByEmail(email);
//        if (appUser != null) {
//            return User.withUsername(appUser.getEmail())
//                    .password(appUser.getPassword())
//                    .roles("student")
//                    .build();
//        }
//
//        Teacher teacher = teacherRepository.findByEmail(email);
//        if (teacher != null) {
//            return User.withUsername(teacher.getEmail())
//                    .password(teacher.getPassword())
//                    .roles("teacher")
//                    .build();
//        }
//
//        throw new UsernameNotFoundException("User not found with email: " + email);
//    }
//}
//
