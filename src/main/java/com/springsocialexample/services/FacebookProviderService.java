package com.springsocialexample.services;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
@Getter
public class FacebookProviderService implements BaseProviderService{
    private static final String FACEBOOK = "facebook";

    private String accessToken;
    private OAuth2Operations oAuth2Operations;

    public FacebookProviderService(@Value("${spring.social.facebook.appId}") String facebookAppId,
            @Value("${spring.social.facebook.appSecret}") String facebookSecret) {
        this.oAuth2Operations = new FacebookConnectionFactory(facebookAppId, facebookSecret).getOAuthOperations();
    }

    @Override
    public String createAuthorizationURL(String url, String scope) throws ProviderConnectionException {
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(url);
        params.setScope(scope);
        return getOAuth2Operations().buildAuthorizeUrl(params);
    }

    @Override
    public UserBean populateUserDetailsFromProvider(Facebook facebook) {
        User user = facebook.userOperations().getUserProfile();
        UserBean userBean = new UserBean();
        userBean.setEmail(user.getEmail());
        userBean.setFirstName(user.getFirstName());
        userBean.setLastName(user.getLastName());
        userBean.setImage(user.getCover().getSource());
        userBean.setProvider(FACEBOOK);
        return userBean;
    }

    public String getAccessToken(String code, String url) {
        try{
            AccessGrant accessGrant = getOAuth2Operations().exchangeForAccess(code, url, null);
            accessToken = accessGrant.getAccessToken();

        } catch (Exception e){
            e.printStackTrace();
        }
        return getAccessToken();
    }

    public User getName() {
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {"name", "email", "timezone"};
        return facebook.fetchObject("me", User.class, fields);
    }
}
