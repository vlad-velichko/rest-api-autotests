package com.rest.qa;

@FunctionalInterface
public interface Supplier<T> {
    T get() throws Exception;
}

