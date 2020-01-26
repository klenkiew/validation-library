package com.validation.lib.validator;

import com.validation.lib.validation.ErrorMessage;
import com.google.common.collect.ImmutableMap;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.valueOf;

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

    public static Function<Long, Optional<ErrorMessage>> odd() {
        return condition(value -> value%2 != 0, ErrorKeys.NOT_ODD);
    }

    public static Function<Long, Optional<ErrorMessage>> between(long min, long max) {
        return condition(
            value -> value >= min && value <= max,
            new ErrorMessage(ErrorKeys.NOT_IN_RANGE, ImmutableMap.of("min", valueOf(min), "max", valueOf(max)))
        );
    }

    public static <T> Function<T, Optional<ErrorMessage>> condition(Function<T, Boolean> condition, String errorKey) {
        return condition(condition, new ErrorMessage(errorKey));
    }

    public static <T> Function<T, Optional<ErrorMessage>> condition(
        Function<T, Boolean> condition, ErrorMessage errorMessage) {

        return value -> !condition.apply(value) ? Optional.of(errorMessage) : Optional.empty();
    }

    private CommonValidators() { }
}
