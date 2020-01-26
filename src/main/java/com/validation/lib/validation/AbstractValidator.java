package com.validation.lib.validation;

import java.util.List;

public abstract class AbstractValidator<T> implements Validator<T> {

    private final List<Validation<T>> validations;

    public AbstractValidator() {
        validations = getValidations();
    }

    @Override
    public void validate(T object) {
        for (Validation<T> validation : validations) {
            validation.validate(object).ifPresent(error -> { throw new ValidationException(error); });
        }
    }

    protected abstract List<Validation<T>> getValidations();
}
