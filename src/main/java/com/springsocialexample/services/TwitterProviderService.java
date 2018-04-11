package com.springsocialexample.services;

import com.springsocialexample.exceptions.InvalidTokenException;
import com.springsocialexample.exceptions.ProviderConnectionException;
import com.springsocialexample.models.UserBean;
import com.springsocialexample.utility.ErrorCode;
import com.springsocialexample.utility.SocialType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Getter
public class TwitterProviderService extends BaseProviderService<Twitter> {

    private OAuth1Operations oAuth1Operations;
    private OAuthToken oAuthToken;

    public TwitterProviderService(@Value("${spring.social.twitter.appId}") String appId,
                                  @Value("${spring.social.twitter.appSecret}") String appSecret) {
        this.oAuth1Operations = new TwitterConnectionFactory(appId, appSecret).getOAuthOperations();
    }

    @Override
    public String createAuthorizationURL(String url, String requestToken) throws ProviderConnectionException {
        OAuth1Parameters params = new OAuth1Parameters();
        params.setCallbackUrl(url);
        if (StringUtils.isEmpty(oAuthToken)) {
            oAuthToken = oAuth1Operations.fetchRequestToken(url, null);
        }

        return getOAuth1Operations().buildAuthorizeUrl(oAuthToken.getValue(), params);
    }

    public String getAccessToken(String token, String verifier) {
        OAuthToken accessToken = getOAuth1Operations().exchangeForAccessToken(new AuthorizedRequestToken(this.oAuthToken, verifier), null);
        return accessToken.getValue() + " | " + accessToken.getSecret();
    }

    public UserBean getUserProfile(String accessToken, String accessTokenSecret) throws InvalidTokenException {
        return populateUserDetailsFromProvider(new TwitterTemplate("3BBuR3EiOzwjjh4wDMOVr7E9N", "wTZDICHAgNmKWQ7BtdSefRePZOAL88nfJkp4hIb0gARLlFugAA", accessToken, accessTokenSecret));
    }

    @Override
    protected UserBean populateUserDetailsFromProvider(Twitter providerObject) throws InvalidTokenException {
        try {
            TwitterProfile userProfile = providerObject.userOperations().getUserProfile();
            return UserBean.builder()
                    .email(userProfile.getScreenName())
                    .firstName(userProfile.getName())
                    .lastName(userProfile.getName())
                    .image(userProfile.getProfileImageUrl())
                    .provider(SocialType.TWITTER.getSnsCode())
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InvalidTokenException(ErrorCode.INVALID_ACCESS_TOKEN.getErrorId(), ErrorCode.INVALID_ACCESS_TOKEN.getErrorMessage());
        }
    }
}
