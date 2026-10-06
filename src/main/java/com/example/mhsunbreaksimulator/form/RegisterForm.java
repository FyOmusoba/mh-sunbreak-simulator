package com.example.mhsunbreaksimulator.form;

import lombok.Data;

@Data
public class RegisterForm {

    private String username;

    private String password;

    private String confirmPassword;
}