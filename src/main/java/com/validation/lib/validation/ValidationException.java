package com.validation.lib.validation;

public class ValidationException extends RuntimeException {

    private final ErrorDescription errorDescription;

    public ValidationException(ErrorDescription errorDescription) {
        this.errorDescription = errorDescription;
    }

    public ErrorDescription getErrorDescription() {
        return errorDescription;
    }
}
