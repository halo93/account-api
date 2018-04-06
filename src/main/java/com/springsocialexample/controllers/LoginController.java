package com.springsocialexample.controllers;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.Result;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.services.BaseProviderService;
import com.springsocialexample.utility.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    BaseProviderService facebookProviderService;

    private final ProviderSignInUtils signInUtils;

    @Autowired
    public LoginController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository) {
        signInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    }

    @GetMapping("/facebook")
    public Result<UserBean> loginViaFacebook(){
        try {
            ProviderSignInUtils
            UserBean userBeanData = facebookProviderService.getUserBeanData();
            return new Result<>(ResultCode.OK.getCode(), ResultCode.OK.getMessage(), userBeanData);
        } catch (ProviderConnectionException e) {
            return new Result<>(e.getErrorCode(), e.getMessage(), null);
        } catch (Exception e) {
            return new Result<>(e.getMessage(), ResultCode.FAIL_UNRECOGNIZED_ERROR.getMessage(), null);
        }
    }
}
