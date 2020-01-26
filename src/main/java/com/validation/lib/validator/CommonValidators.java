package com.validation.lib.validator;

import com.validation.lib.validation.ErrorMessage;
import com.google.common.collect.ImmutableMap;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.valueOf;
import static java.util.Optional.*;

public class CommonValidators {

    public static <T> Function<T, Optional<ErrorMessage>> notNull() {
        return condition(Objects::nonNull, ErrorKeys.VALUE_REQUIRED);
    }

    public static Function<String, Optional<ErrorMessage>> notBlank() {
        return condition(value -> value != null && !"".equals(value), ErrorKeys.VALUE_REQUIRED);
    }

    public static Function<Long, Optional<ErrorMessage>> even() {
        return condition(value -> value%2 == 0, ErrorKeys.NOT_EVEN);
    }

    public static Function<Long, Optional<ErrorMessage>> between(long min, long max) {
        return value -> value < min || value > max
            ? of(new ErrorMessage(ErrorKeys.NOT_IN_RANGE, ImmutableMap.of("min", valueOf(min), "max", valueOf(max))))
            : empty();
    }

    public static <T> Function<T, Optional<ErrorMessage>> condition(Function<T, Boolean> condition, String errorKey) {
        return value -> !condition.apply(value)
            ? of(new ErrorMessage(errorKey))
            : empty();
    }

    private CommonValidators() { }
}
