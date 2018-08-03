package de.maxdobler.fluentvalidator;

@FunctionalInterface
public interface Validator<T, E> {
    ValidatorResult<E> validate(T value);
}
