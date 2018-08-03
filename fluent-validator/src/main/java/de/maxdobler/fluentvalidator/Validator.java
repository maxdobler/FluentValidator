package de.maxdobler.fluentvalidator;

@FunctionalInterface
public interface Validator<T> {
    ValidatorResult validate(T value);
}
