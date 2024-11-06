package com.hello.school_management.Models;

import jakarta.validation.constraints.*;


public class RegisterDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @Size(min = 11, max = 11, message = "Phone Number should be 11 digit")
    private String phone;

    private String address;

    @Size(min = 6, message = "Minimum Password length is 6 characters")
    private  String password;

    private String confirmPassword;

    public @NotEmpty String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotEmpty String firstName) {
        this.firstName = firstName;
    }

    public @NotEmpty String getLastName() {
        return lastName;
    }

    public void setLastName(@NotEmpty String lastName) {
        this.lastName = lastName;
    }

    public @NotEmpty @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty @Email String email) {
        this.email = email;
    }

    public @Size(min = 11, max = 11, message = "Phone Number should be 11 digit") String getPhone() {
        return phone;
    }

    public void setPhone(@Size(min = 11, max = 11, message = "Phone Number should be 11 digit") String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public @Size(min = 6, message = "Minimum Password length is 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6, message = "Minimum Password length is 6 characters") String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

