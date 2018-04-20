package com.springsocialexample.models;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class UserBean implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String title;
    private String country;
    private String password;
    private String passwordConfirm;
    private String provider;
    private String image;
    private String apiToken;
}
