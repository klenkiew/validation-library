package com.validation.lib.validation;

import com.google.common.base.Suppliers;
import com.validation.lib.extractor.PropertyNameExtractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ValidationBuilder<T> {

    private final Supplier<PropertyNameExtractor<T>> propertyNameExtractorSupplier;
    private final List<Validation<T>> validations = new ArrayList<>();

    private ValidationBuilder(Class<T> clazz) {
        propertyNameExtractorSupplier = Suppliers.memoize(() -> new PropertyNameExtractor<>(clazz));
    }

    public static <T> ValidationBuilder<T> forClass(Class<T> clazz) {
        return new ValidationBuilder<>(clazz);
    }

    public <F> FieldValidationBuilder<F> ruleFor(Function<T, F> fieldGetter, String fieldName) {
        return new FieldValidationBuilder<>(fieldGetter, fieldName);
    }

    public <F> FieldValidationBuilder<F> ruleFor(Function<T, F> fieldGetter) {
        return new FieldValidationBuilder<>(fieldGetter, propertyNameExtractorSupplier.get().getPropertyName(fieldGetter));
    }

    @SafeVarargs
    public final ValidationBuilder<T> require(Function<T, Optional<ErrorMessage>>... conditions) {
        List<Validation<T>> validations = Arrays.stream(conditions)
            .map(condition -> (Validation<T>) object -> condition.apply(object).map(ErrorDescription::new))
            .collect(Collectors.toList());
        this.validations.addAll(validations);
        return this;
    }

    public List<Validation<T>> build() {
        return validations;
    }

    public class FieldValidationBuilder<F> {

        private final Function<T, F> fieldGetter;
        private final String fieldName;

        public FieldValidationBuilder(Function<T, F> fieldGetter, String fieldName) {
            this.fieldGetter = fieldGetter;
            this.fieldName = fieldName;
        }

        @SafeVarargs
        public final FieldValidationBuilder<F> require(Function<F, Optional<ErrorMessage>>... conditions) {
            List<Validation<T>> validations = Arrays.stream(conditions)
                .map(condition -> (Validation<T>) object -> condition.apply(fieldGetter.apply(object))
                    .map(msg -> new ErrorDescription(msg, fieldName)))
                .collect(Collectors.toList());
            ValidationBuilder.this.validations.addAll(validations);
            return this;
        }

        public ValidationBuilder<T> and() {
            return ValidationBuilder.this;
        }
    }
}
