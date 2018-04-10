package com.springsocialexample.services;

import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.utility.ServiceUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
@Getter
public class FacebookProviderService extends BaseProviderService<Facebook> {
    private static final String FACEBOOK = "facebook";

    @Setter
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

    public String getAccessToken(String code, String url) {
        AccessGrant accessGrant = getOAuth2Operations().exchangeForAccess(code, url, null);
        setAccessToken(accessGrant.getAccessToken());
        return getAccessToken();
    }

    public UserBean getUserProfile() {
        return populateUserDetailsFromProvider(new FacebookTemplate(accessToken));
    }

    @Override
    protected UserBean populateUserDetailsFromProvider(Facebook providerObject) {
        String [] fields = { "id", "email",  "first_name", "last_name", "picture"};
        User user = providerObject.fetchObject("me", User.class, fields);
        return UserBean.builder()
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .image(ServiceUtil.fetchPictureUrl(user.getId(), ImageType.SQUARE))
            .provider(FACEBOOK)
            .build();
    }
}
