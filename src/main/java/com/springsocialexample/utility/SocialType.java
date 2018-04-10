package com.springsocialexample.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SocialType {
    FACEBOOK("facebook"),
    TWITTER("twitter");

    private final String snsCode;
}
