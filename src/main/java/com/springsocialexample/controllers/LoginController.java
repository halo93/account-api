package com.springsocialexample.controllers;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.services.FacebookProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {
    private FacebookProviderService facebookProviderService;

    @Autowired
    public LoginController(FacebookProviderService facebookProviderService) {
        this.facebookProviderService = facebookProviderService;
    }

    @GetMapping("login/facebook")
    public String loginViaFacebook() {
        try {
            return facebookProviderService.createAuthorizationURL("http://localhost:8080/facebook", null);
        } catch (ProviderConnectionException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("/facebook")
    public String createFacebookAccessToken(@RequestParam("code") String code){
        return facebookProviderService.getAccessToken(code, "http://localhost:8080/facebook");
    }

    @GetMapping("/getName")
    public User getNameResponse(){
        return facebookProviderService.getName();
    }
}
