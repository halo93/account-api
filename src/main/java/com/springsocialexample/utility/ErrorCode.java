package com.springsocialexample.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ALREADY_LOGGED_IN("E001", "Already logged in via social network!"),
    INVALID_ACCESS_TOKEN("E002", "Invalid Access Token");

    private final String errorId;
    private final String errorMessage;
}
