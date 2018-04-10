package com.springsocialexample.services;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.models.UserBean;

public abstract class BaseProviderService<P> implements IBaseProviderService {
    protected abstract UserBean populateUserDetailsFromProvider(P providerObject) throws InvalidTokenException;
}
