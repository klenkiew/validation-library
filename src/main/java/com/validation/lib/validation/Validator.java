package com.validation.lib.validation;

public interface Validator<T> {
    void validate(T object);
}
