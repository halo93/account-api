package com.springsocialexample.services;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;

public interface IBaseProviderService<P> {
    String createAuthorizationURL(String url) throws ProviderConnectionException;
    UserBean populateUserDetailsFromProvider(P providerObject) throws InvalidTokenException;
    String getAccessToken(String code, String uri);
    UserBean getUserProfile(String accessToken, String accessTokenSecret) throws InvalidTokenException;
    boolean supports(String providerName);
}
