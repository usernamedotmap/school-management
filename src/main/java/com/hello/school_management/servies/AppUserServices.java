package com.hello.school_management.servies;

import com.hello.school_management.Models.AppUser;
import com.hello.school_management.Models.Role; // Import the Role enum
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hello.school_management.servies.AppUserRepository;

import java.util.Collections;

@Service
public class AppUserServices implements UserDetailsService {

    @Autowired
    private AppUserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = repo.findByEmail(email);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name());
        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .authorities(Collections.singletonList(authority))
                .build();
    }
//
    public void registerUser(AppUser newUser, PasswordEncoder passwordEncoder) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        repo.save(newUser);
    }
}