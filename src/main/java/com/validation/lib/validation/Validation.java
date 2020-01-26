package com.validation.lib.validation;

import java.util.Optional;

public interface Validation<T> {

    Optional<ErrorDescription> validate(T object);
}
