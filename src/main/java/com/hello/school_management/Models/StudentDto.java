package com.hello.school_management.Models;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class StudentDto {

    @NotEmpty(message = "The name is required")
    private String name;


    @Min(value = 15, message = "The age must be at least 15")
    private int age;

    @NotEmpty(message = "The email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotEmpty(message = "The address is required")
    private String address;

    @NotEmpty(message = "The status is required")
    private String status;

    private MultipartFile imageFile;

    // Getters and setters...
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
