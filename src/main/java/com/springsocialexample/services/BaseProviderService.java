package com.springsocialexample.services;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.utility.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseProviderService {
    @Autowired
    BaseProvider baseProvider;

    protected void connectViaProvider(Class clazz) throws ProviderConnectionException {
        ConnectionRepository connectionRepository = baseProvider.getConnectionRepository();
        if (connectionRepository.findPrimaryConnection(clazz) != null) {
            throw new ProviderConnectionException(ErrorCode.ALREADY_LOGGED_IN.getErrorId(),
                ErrorCode.ALREADY_LOGGED_IN.getErrorMessage());
        }
    }

    public abstract UserBean getUserBeanData() throws ProviderConnectionException;
    protected abstract UserBean populateUserDetailsFromProvider();
}
