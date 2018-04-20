package com.springsocialexample.services;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.security.JWTSecurityUtil;
import com.springsocialexample.utility.ErrorCode;
import com.springsocialexample.utility.ServiceUtil;
import com.springsocialexample.utility.SocialType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
@Getter
public class FacebookProviderService implements IBaseProviderService<Facebook> {

    public static final String DEFAULT_PERMISSION = "public_profile, email";

    private OAuth2Operations oAuth2Operations;

    public FacebookProviderService(@Value("${spring.social.facebook.appId}") String facebookAppId,
                                   @Value("${spring.social.facebook.appSecret}") String facebookSecret) {
        this.oAuth2Operations = new FacebookConnectionFactory(facebookAppId, facebookSecret).getOAuthOperations();
    }

    @Override
    public String createAuthorizationURL(GrantType grantType, String url) throws ProviderConnectionException {
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(url);
        params.setScope(DEFAULT_PERMISSION);
        return getOAuth2Operations().buildAuthorizeUrl(GrantType.IMPLICIT_GRANT, params);
    }

    @Override
    public String getAccessToken(String code, String uri) {
        return getOAuth2Operations().exchangeForAccess(code, uri, null).getAccessToken();
    }

    @Override
    public UserBean getUserProfile(String accessToken, String accessTokenSecret) throws InvalidTokenException {
        return getFbUserProfile(accessToken);
    }

    @Override
    public UserBean populateUserDetailsFromProvider(Facebook providerObject) throws InvalidTokenException {
        String[] fields = {"id", "email", "first_name", "last_name", "picture"};
        try {
            User user = providerObject.fetchObject("me", User.class, fields);
            return UserBean.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .image(ServiceUtil.fetchPictureUrl(user.getId(), ImageType.SQUARE))
                    .provider(SocialType.FACEBOOK.getSnsCode())
                    .apiToken(JWTSecurityUtil.generateApiToken(user.getEmail()))
                    .build();
        } catch (Exception ex) {
            throw new InvalidTokenException(ErrorCode.INVALID_ACCESS_TOKEN.getErrorId(), ErrorCode.INVALID_ACCESS_TOKEN.getErrorMessage());
        }
    }

    @Override
    public boolean supports(String providerName) {
        return SocialType.FACEBOOK.getSnsCode().equals(providerName);
    }

    private UserBean getFbUserProfile(String accessToken) throws InvalidTokenException {
        return populateUserDetailsFromProvider(new FacebookTemplate(accessToken));
    }
}
