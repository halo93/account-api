package com.springsocialexample.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProviderConnectionException extends Exception {
    private static final long serialVersionUID = 1L;

    private String errorCode;
    public ProviderConnectionException(String message) {
        super(message);
    }

    public ProviderConnectionException(String errorCode, String message) {
        super(message);
        this.setErrorCode(errorCode);
    }
}
