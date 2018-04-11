package com.springsocialexample.controllers;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.Result;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.services.FacebookProviderService;
import com.springsocialexample.services.TwitterProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginController {

    @Value("${server.port}")
    private String serverPort;

    private FacebookProviderService facebookProviderService;
    private TwitterProviderService twitterProviderService;

    @Autowired
    public LoginController(FacebookProviderService facebookProviderService, TwitterProviderService twitterProviderService) {
        this.facebookProviderService = facebookProviderService;
        this.twitterProviderService = twitterProviderService;
    }

    @GetMapping("login/facebook")
    public String loginViaFacebook() {
        try {
            return facebookProviderService.createAuthorizationURL(String.format("http://localhost:%s/facebook", serverPort), "email");
        } catch (ProviderConnectionException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("/facebook")
    public String createFacebookAccessToken(@RequestParam("code") String code) {
        return facebookProviderService.getAccessToken(code, String.format("http://localhost:%s/facebook", serverPort));
    }

    @GetMapping("/facebook/get-user-profile")
    public Result<UserBean> getFacebookUserProfile(@RequestParam("access-token") String accessToken) {
        try {
            return new Result<>(HttpStatus.OK.toString(), null, facebookProviderService.getUserProfile(accessToken));
        } catch (InvalidTokenException e) {
            return new Result<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
        }
    }

    @GetMapping("login/twitter")
    public String loginViaTwitter() {
        try {
            return twitterProviderService.createAuthorizationURL(String.format("http://localhost:%s/twitter", serverPort), "email");
        } catch (ProviderConnectionException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("/twitter")
    public String createTwitterAccessToken(@RequestParam("oauth_token") String token, @RequestParam("oauth_verifier") String verifier) {
        return twitterProviderService.getAccessToken(token, verifier);
    }

    @GetMapping("/twitter/get-user-profile")
    public Result<UserBean> getUserProfile(@RequestParam("access-token") String accessToken, @RequestParam("access-token-secret") String accessTokenSecret) {
        try {
            return new Result<>(HttpStatus.OK.toString(), null, twitterProviderService.getUserProfile(accessToken, accessTokenSecret));
        } catch (InvalidTokenException e) {
            return new Result<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
        }
    }
}
