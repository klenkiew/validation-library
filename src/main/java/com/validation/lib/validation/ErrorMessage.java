package com.validation.lib.validation;

import java.util.Map;

public class ErrorMessage {

    private final String errorKey;
    private final Map<String, String> parameters;

    public ErrorMessage(String errorKey) {
        this(errorKey, null);
    }

    public ErrorMessage(String errorKey, Map<String, String> parameters) {
        this.errorKey = errorKey;
        this.parameters = parameters;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
