package com.springsocialexample.controllers;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.Result;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.services.factory.SnsServiceFactory;
import com.springsocialexample.utility.SocialType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {

    @Value("${server.domain}")
    private String serverDomain;

    @Value("${server.servlet.context-path}")
    private String rootContextPath;

    @Autowired
    private SnsServiceFactory snsServiceFactory;

    @GetMapping("/sns/start/via/{sns_code}")
    public Result<String> startLoginViaSns(@PathVariable("sns_code") String snsCode) {
        try {
            return new Result<>(HttpStatus.OK.toString(), 
                HttpStatus.OK.getReasonPhrase(), 
//                snsServiceFactory.get(snsCode).createAuthorizationURL(String.format("%s%s/%s", serverDomain, rootContextPath, snsCode)));
                snsServiceFactory.get(snsCode).createAuthorizationURL(String.format("https://9f5c42d7.ngrok.io", snsCode)));
        } catch (ProviderConnectionException e) {
            return new Result<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
        }
    }

    @GetMapping("/{sns_code}/get-user-profile")
    public Result<UserBean> getUserProfile(@PathVariable("sns_code") String snsCode,
                                           @RequestParam("access-token") String accessToken,
                                           @RequestParam(value = "access-token-secret", required = false) String accessTokenSecret) {
        return StringUtils.isEmpty(accessTokenSecret) ? getOAuth2UserProfile(snsCode, accessToken) : getOAuth1UserProfile(snsCode, accessToken, accessTokenSecret);
    }

    @GetMapping("/facebook")
    public Result<String> createFacebookAccessToken(@RequestParam("code") String code) {
        return new Result<>(HttpStatus.OK.toString(),
            HttpStatus.OK.getReasonPhrase(),
            snsServiceFactory.get(SocialType.FACEBOOK.getSnsCode())
                    .getAccessToken(code, String.format("%s%s/facebook", serverDomain, rootContextPath)));
    }

    @GetMapping("/twitter")
    public Result<String> createTwitterAccessToken(@RequestParam("oauth_token") String token, @RequestParam("oauth_verifier") String verifier) {
        return new Result<>(HttpStatus.OK.toString(),
            HttpStatus.OK.getReasonPhrase(),
            snsServiceFactory.get(SocialType.TWITTER.getSnsCode()).getAccessToken(token, verifier));
    }

    private Result<UserBean> getOAuth2UserProfile(String snsCode, String accessToken) {
        try {
            return new Result<>(HttpStatus.OK.toString(), HttpStatus.OK.getReasonPhrase(), snsServiceFactory.get(snsCode).getUserProfile(accessToken, null));
        } catch (InvalidTokenException e) {
            return new Result<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
        }
    }

    private Result<UserBean> getOAuth1UserProfile(String snsCode, String accessToken, String accessTokenSecret) {
        try {
            return new Result<>(HttpStatus.OK.toString(), null, snsServiceFactory.get(snsCode).getUserProfile(accessToken, accessTokenSecret));
        } catch (InvalidTokenException e) {
            return new Result<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
        }
    }

}
