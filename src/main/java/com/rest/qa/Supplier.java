// Overriding of common Supplier functional interface just for exceptions throwing

package com.rest.qa;

@FunctionalInterface
public interface Supplier<T> {
    T get() throws Exception;
}

