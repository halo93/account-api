package com.springsocialexample.utility;

import lombok.Getter;

@Getter
public enum ResultCode {
    OK("200", "OK"),
    FAIL_UNRECOGNIZED_ERROR("999","UNRECOGNIZED ERROR");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
