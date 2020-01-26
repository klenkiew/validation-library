package com.validation.lib.extractor;

import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyNameExtractorTests {

    @Test
    public void testForStringGetter() {
        testProperty(Employee::getFirstName, "firstName");
    }

    @Test
    public void testForClassGetter() {
        testProperty(Employee::getManager, "manager");
    }

    @Test
    public void testForPrimitiveTypeGetter() {
        testProperty(Employee::getWage, "wage");
    }

    @Test
    public void testForStringSetter() {
        testProperty(Employee::setFirstName, "firstName");
    }

    @Test
    public void testForClassSetter() {
        testProperty(Employee::setManager, "manager");
    }

    private void testProperty(Function<Employee, ?> getterFunction, String expectedName) {
        String propertyName = new PropertyNameExtractor<>(Employee.class).getPropertyName(getterFunction);
        assertThat(propertyName).isEqualTo(expectedName);
    }

    private <T> void testProperty(BiConsumer<Employee, T> setterFunction, String expectedName) {
        String propertyName = new PropertyNameExtractor<>(Employee.class).getPropertyName(setterFunction);
        assertThat(propertyName).isEqualTo(expectedName);
    }

    static class Employee {

        private String firstName;
        private final String lastName;
        private final long wage;
        private Employee manager = null;

        public Employee(String firstName, String lastName, long wage) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.wage = wage;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public long getWage() {
            return wage;
        }

        public Employee getManager() {
            return manager;
        }

        public void setManager(Employee manager) {
            this.manager = manager;
        }
    }
}
