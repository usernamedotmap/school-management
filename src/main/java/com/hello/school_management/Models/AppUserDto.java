package com.hello.school_management.Models;

import jakarta.validation.constraints.*;

public class AppUserDto {

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    private String email;

    @NotEmpty(message = "Address is required")
    private String address;

    @NotEmpty(message = "Phone is required")
    private int phone;

    public @NotEmpty(message = "First name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotEmpty(message = "First name is required") String firstName) {
        this.firstName = firstName;
    }

    public @NotEmpty(message = "Last name is required") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotEmpty(message = "Last name is required") String lastName) {
        this.lastName = lastName;
    }

    public @Email(message = "Please provide a valid email address") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Please provide a valid email address") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(@NotEmpty(message = "Address is required") String address) {
        this.address = address;
    }

    @NotEmpty(message = "Phone is required")
    public int getPhone() {
        return phone;
    }

    public void setPhone(@NotEmpty(message = "Phone is required") int phone) {
        this.phone = phone;
    }
}
