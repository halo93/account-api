package com.springsocialexample.services;

import com.springsocialexample.models.UserBean;

public abstract class BaseProviderService<P> implements IBaseProviderService {
    protected abstract UserBean populateUserDetailsFromProvider(P providerObject);
}
