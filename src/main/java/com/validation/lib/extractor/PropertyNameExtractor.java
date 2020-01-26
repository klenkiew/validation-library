package com.validation.lib.extractor;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PropertyNameExtractor<T> {

    private final MethodInvocationRecorder<T> invocationRecorder;

    public PropertyNameExtractor(Class<T> clazz) {
        invocationRecorder = new MethodInvocationRecorder<>(clazz);
    }

    public <F> String getPropertyName(Function<T, F> getter) {
        Method method = invocationRecorder.getInvokedMethod(getter::apply);
        return extractPropertyName(method);
    }

    public <F> String getPropertyName(BiConsumer<T, F> setter) {
        // TODO handle primitive types for setters (for now it's NPE because we pass null to the setter)
        Method method = invocationRecorder.getInvokedMethod(o -> setter.accept(o, null));
        return extractPropertyName(method);
    }

    private String extractPropertyName(Method method) {
        String methodName = method.getName();
        if (!isProperty(method))
            throw new RuntimeException("Method " + methodName + " is not a getter or setter");
        return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }

    private boolean isProperty(Method method) {
        String methodName = method.getName();
        return methodName.startsWith("get") || methodName.startsWith("set");
    }
}
