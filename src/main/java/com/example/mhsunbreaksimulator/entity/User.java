package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class User {

    private Integer userId;

    private String username;

    private String passwordHash;
}