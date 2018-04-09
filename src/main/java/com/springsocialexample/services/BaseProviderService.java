package com.springsocialexample.services;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.facebook.api.Facebook;

public interface BaseProviderService {
    String createAuthorizationURL(String url, String scope) throws ProviderConnectionException;
    UserBean populateUserDetailsFromProvider(Facebook facebook);
}
