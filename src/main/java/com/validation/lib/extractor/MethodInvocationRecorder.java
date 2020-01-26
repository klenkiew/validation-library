package com.validation.lib.extractor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.EmptyTargetSource;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class MethodInvocationRecorder<T> {

    private Method invokedMethod;
    private final T recordingProxy;

    public MethodInvocationRecorder(Class<T> clazz) {
        MethodInterceptor interceptor = methodInvocation -> {
            invokedMethod = methodInvocation.getMethod();
            return DefaultValues.getDefault(methodInvocation.getMethod().getReturnType());
        };
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTargetSource(EmptyTargetSource.forClass(clazz));
        proxyFactory.addAdvice(interceptor);
        //noinspection unchecked
        recordingProxy = (T) proxyFactory.getProxy();
    }

    public Method getInvokedMethod(Consumer<T> invocation) {
        invokedMethod = null;
        invocation.accept(recordingProxy);
        return invokedMethod;
    }
}
