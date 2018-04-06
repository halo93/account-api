package com.springsocialexample.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  ErrorCode {
    ALREADY_LOGGED_IN("E001", "Already logged in via social network!");

    private final String errorId;
    private final String errorMessage;
}
