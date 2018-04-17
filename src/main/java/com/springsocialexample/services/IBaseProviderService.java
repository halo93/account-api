package com.springsocialexample.services;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import org.springframework.social.oauth2.GrantType;

public interface IBaseProviderService<P> {
    String createAuthorizationURL(GrantType grantType, String url) throws ProviderConnectionException;
    UserBean populateUserDetailsFromProvider(P providerObject) throws InvalidTokenException;
    String getAccessToken(String code, String uri);
    UserBean getUserProfile(String accessToken, String accessTokenSecret) throws InvalidTokenException;
    boolean supports(String providerName);
}
