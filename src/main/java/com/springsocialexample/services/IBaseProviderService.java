package com.springsocialexample.services;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;

public interface IBaseProviderService {
    String createAuthorizationURL(String url, String scope) throws ProviderConnectionException;
}
