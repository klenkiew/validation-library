package com.validation.lib.validation;

public class ErrorDescription {

    private final ErrorMessage errorMessage;
    private final String field;

    public ErrorDescription(ErrorMessage errorMessage) {
        this(errorMessage, null);
    }

    public ErrorDescription(ErrorMessage errorMessage, String field) {
        this.errorMessage = errorMessage;
        this.field = field;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public String getField() {
        return field;
    }
}
