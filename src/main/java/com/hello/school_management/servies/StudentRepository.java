package com.hello.school_management.servies;

import com.hello.school_management.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByEmail(String email);
    String findByEmail(String email);

}
