package com.springsocialexample.controllers;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.Result;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.security.JWTSecurityUtil;
import com.springsocialexample.services.factory.SnsServiceFactory;
import com.springsocialexample.utility.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.social.oauth2.GrantType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.springsocialexample.utility.SocialType.FACEBOOK;
import static com.springsocialexample.utility.SocialType.TWITTER;

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
            return getAuthorizationUrlResult(snsCode);
        } catch (ProviderConnectionException e) {
            return new Result<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
        }
    }

    @GetMapping("/sns/exchange/{sns_code}/token")
    public Result<UserBean> startLoginViaSns(@PathVariable("sns_code") String snsCode,
                                             @RequestParam("access_token") String accessToken,
                                             @RequestParam(value = "access_token_secret", required = false) String accessTokenSecret) {
        UserBean userProfile = getUserProfile(snsCode, accessToken, accessTokenSecret);
        if (userProfile == null) {
            return new Result<UserBean>(HttpStatus.BAD_REQUEST.toString(), ErrorCode.INVALID_ACCESS_TOKEN.getErrorMessage(), null);
        }
        return new Result<UserBean>(HttpStatus.OK.toString(), HttpStatus.OK.getReasonPhrase(), userProfile);
    }

    private UserBean getUserProfile(String snsCode, String accessToken, String accessTokenSecret) {
        return StringUtils.isEmpty(accessTokenSecret) ? getOAuth2UserProfile(snsCode, accessToken) : getOAuth1UserProfile(snsCode, accessToken, accessTokenSecret);
    }

    @GetMapping("/facebook")
    public Result<String> createFacebookAccessToken(@RequestParam("code") String code) {
        return new Result<>(HttpStatus.OK.toString(),
                HttpStatus.OK.getReasonPhrase(),
                snsServiceFactory.get(FACEBOOK.getSnsCode())
                        .getAccessToken(code, String.format("%s%s/facebook", serverDomain, rootContextPath)));
    }

    @GetMapping("/twitter")
    public Result<String> createTwitterAccessToken(@RequestParam("oauth_token") String token, @RequestParam("oauth_verifier") String verifier) {
        return new Result<>(HttpStatus.OK.toString(),
                HttpStatus.OK.getReasonPhrase(),
                snsServiceFactory.get(TWITTER.getSnsCode()).getAccessToken(token, verifier));
    }

    private UserBean getOAuth2UserProfile(String snsCode, String accessToken) {
        try {
            return snsServiceFactory.get(snsCode).getUserProfile(accessToken, null);
        } catch (InvalidTokenException e) {
            return null;
        }
    }

    private UserBean getOAuth1UserProfile(String snsCode, String accessToken, String accessTokenSecret) {
        try {
            return snsServiceFactory.get(snsCode).getUserProfile(accessToken, accessTokenSecret);
        } catch (InvalidTokenException e) {
            return null;
        }
    }

    private Result<String> getAuthorizationUrlResult(String snsCode)
            throws ProviderConnectionException {
        String authorizationURL = "";
        switch (snsCode) {
            case "facebook":
                authorizationURL = snsServiceFactory.get(snsCode).createAuthorizationURL(GrantType.IMPLICIT_GRANT, "https://localhostss.com:9000/success.html");
                break;
            case "twitter":
                authorizationURL = snsServiceFactory.get(snsCode).createAuthorizationURL(null, "https://localhostss.com:9000/success.html");
                break;
            default:
                return new Result<>(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
        }
        return new Result<>(HttpStatus.OK.toString(), HttpStatus.OK.getReasonPhrase(), authorizationURL);
    }
}
