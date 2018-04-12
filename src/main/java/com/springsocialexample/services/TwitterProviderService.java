package com.springsocialexample.services;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.models.twittermodelwrapper.TwitterProfileWrapper;
import com.springsocialexample.utility.ErrorCode;
import com.springsocialexample.utility.SocialType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Data
public class TwitterProviderService implements IBaseProviderService<Twitter> {

    private OAuth1Operations oAuth1Operations;

    private OAuthToken oAuthToken;

    private String appId;

    private String appSecret;

    public TwitterProviderService(@Value("${spring.social.twitter.appId}") String appId,
                                  @Value("${spring.social.twitter.appSecret}") String appSecret) {
        setAppId(appId);
        setAppSecret(appSecret);
        setOAuth1Operations(new TwitterConnectionFactory(appId, appSecret).getOAuthOperations());
    }

    @Override
    public String createAuthorizationURL(String url) throws ProviderConnectionException {
        OAuth1Parameters params = new OAuth1Parameters();
        params.setCallbackUrl(url);
        if (StringUtils.isEmpty(getOAuthToken())) {
            setOAuthToken(getOAuth1Operations().fetchRequestToken(url, null));
        }
        return getOAuth1Operations().buildAuthorizeUrl(getOAuthToken().getValue(), params);
    }

    @Override
    public String getAccessToken(String token, String verifier) {
        OAuthToken accessToken = getOAuth1Operations().exchangeForAccessToken(new AuthorizedRequestToken(getOAuthToken(), verifier), null);
        return accessToken.getValue() + " | " + accessToken.getSecret();
    }

    @Override
    public UserBean getUserProfile(String accessToken, String accessTokenSecret) throws InvalidTokenException {
        return populateUserDetailsFromProvider(new TwitterTemplate(getAppId(), getAppSecret(), accessToken, accessTokenSecret));
    }

    @Override
    public UserBean populateUserDetailsFromProvider(Twitter providerObject) throws InvalidTokenException {
        try {
            //TODO: hack for get email
            TwitterProfileWrapper userProfile = providerObject.restOperations().getForObject("https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true", TwitterProfileWrapper.class);
            return UserBean.builder()
                .email(userProfile.getEmail())
                .firstName(userProfile.getName())
                .image(userProfile.getProfileImageUrl())
                .provider(SocialType.TWITTER.getSnsCode())
                .build();
        } catch (Exception ex) {
            throw new InvalidTokenException(ErrorCode.INVALID_ACCESS_TOKEN.getErrorId(), ErrorCode.INVALID_ACCESS_TOKEN.getErrorMessage());
        }
    }

    @Override
    public boolean supports(String providerName) {
        return SocialType.TWITTER.getSnsCode().equals(providerName);
    }
}
