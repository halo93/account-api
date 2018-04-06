package com.springsocialexample.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String email;
    private String title;
    private String country;
    private String password;
    private String passwordConfirm;
    private String provider;
    private String image;
}
