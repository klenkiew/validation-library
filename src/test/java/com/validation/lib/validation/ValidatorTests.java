package com.validation.lib.validation;

import com.validation.lib.validator.ErrorKeys;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.validation.lib.validator.CommonValidators.*;
import static org.assertj.core.api.Assertions.*;

public class ValidatorTests {

    @Test
    public void testForValid() {
        Employee employee = new Employee("John", "Smith", 12);
        EmployeeValidator validator = new EmployeeValidator();
        assertValid(employee, validator);
    }

    @Test
    public void testNotNull() {
        Employee employee = new Employee("John", null, 12);
        EmployeeValidator validator = new EmployeeValidator();
        assertInvalid(employee, validator, (errorDescription -> {
            assertThat(errorDescription.getField()).isEqualTo("lastName");
            assertThat(errorDescription.getErrorMessage().getErrorKey()).isEqualTo(ErrorKeys.VALUE_REQUIRED);
        }));
    }

    @Test
    public void testNotInRange() {
        Employee employee = new Employee("John", "", 14);
        EmployeeValidator validator = new EmployeeValidator();
        assertInvalid(employee, validator, (errorDescription -> {
            assertThat(errorDescription.getField()).isEqualTo("wage");
            assertThat(errorDescription.getErrorMessage().getErrorKey()).isEqualTo(ErrorKeys.VALUE_REQUIRED);
            assertThat(errorDescription.getErrorMessage().getParameters().get("min")).isEqualTo("6");
            assertThat(errorDescription.getErrorMessage().getParameters().get("max")).isEqualTo("12");
        }));
    }

    @Test
    public void testNotEven() {
        Employee employee = new Employee("John", "Smith", 13);
        EmployeeValidator validator = new EmployeeValidator();
        assertInvalid(employee, validator, (errorDescription -> {
            assertThat(errorDescription.getField()).isEqualTo("wage");
            assertThat(errorDescription.getErrorMessage().getErrorKey()).isEqualTo(ErrorKeys.NOT_EVEN);
        }));
    }

    @Test
    public void testCustomValidatingLambda() {
        Employee employee = new Employee("John", "Smith", 10);
        EmployeeValidator validator = new EmployeeValidator();
        assertInvalid(employee, validator, (errorDescription -> {
            assertThat(errorDescription.getField()).isEqualTo("wage");
            assertThat(errorDescription.getErrorMessage().getErrorKey()).isEqualTo("error.is10");
        }));
    }

    @Test
    public void testCustomCondition() {
        Employee employee = new Employee("John", "Smith", 8);
        EmployeeValidator validator = new EmployeeValidator();
        assertInvalid(employee, validator, (errorDescription -> {
            assertThat(errorDescription.getField()).isEqualTo("wage");
            assertThat(errorDescription.getErrorMessage().getErrorKey()).isEqualTo("error.is8");
        }));
    }

    private <T> void assertValid(T object, AbstractValidator<T> validator) {
        assertThat(catchThrowable(() -> validator.validate(object))).doesNotThrowAnyException();
    }

    private <T> void assertInvalid(T object, AbstractValidator<T> validator, Consumer<ErrorDescription> errorTest) {
        ValidationException exception = catchThrowableOfType(() -> validator.validate(object), ValidationException.class);
        assertThat(exception).isNotNull();
        ErrorDescription errorDescription = exception.getErrorDescription();
        errorTest.accept(errorDescription);
    }

    static class EmployeeValidator extends AbstractValidator<Employee> {

        @Override
        protected List<Validation<Employee>> getValidations() {
            return ValidationBuilder.forClass(Employee.class)

                .ruleFor(Employee::getFirstName)
                .require(notBlank())
                .and()
                .ruleFor(Employee::getLastName)
                .require(notNull())
                .and()
                .ruleFor(Employee::getWage)
                .require(
                    even(),
                    between(6, 12),
                    num -> num == 10 ? Optional.of(new ErrorMessage("error.is10")) : Optional.empty(),
                    condition(num -> num != 8, "error.is8")
                )
                .and()
                .build();
        }
    }

    static class Employee {

        private final String firstName;
        private final String lastName;
        private final long wage;

        public Employee(String firstName, String lastName, long wage) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.wage = wage;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public long getWage() {
            return wage;
        }
    }
}
