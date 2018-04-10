package com.springsocialexample.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidTokenException extends Exception {
    private static final long serialVersionUID = 1L;

    private String errorCode;
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String errorCode, String message) {
        super(message);
        this.setErrorCode(errorCode);
    }
}
