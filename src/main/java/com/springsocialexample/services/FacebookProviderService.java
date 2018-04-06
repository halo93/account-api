package com.springsocialexample.services;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;

@Service
public class FacebookProviderService extends BaseProviderService{
    private static final String FACEBOOK = "facebook";

    @Override
    public UserBean getUserBeanData() throws ProviderConnectionException{
        connectViaProvider(Facebook.class);
        return populateUserDetailsFromProvider();
    }

    @Override
    protected UserBean populateUserDetailsFromProvider() {
        Facebook facebook = baseProvider.getFacebook();
        User user = facebook.userOperations().getUserProfile();
        UserBean userBean = new UserBean();
        userBean.setEmail(user.getEmail());
        userBean.setFirstName(user.getFirstName());
        userBean.setLastName(user.getLastName());
        userBean.setImage(user.getCover().getSource());
        userBean.setProvider(FACEBOOK);
        return userBean;
    }
}
