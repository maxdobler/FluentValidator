package de.maxdobler.fluentvalidator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorResult<T> {
    private List<T> errors;

    private ValidatorResult(T error) {
        this();
        this.errors.add(error);
    }

    private ValidatorResult() {
        this.errors = new ArrayList<>();
    }

    public static <T> ValidatorResult<T> valid() {
        return new ValidatorResult<>();
    }

    public static <T> ValidatorResult<T> error(T error) {
        return new ValidatorResult<>(error);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<T> getErrors() {
        return errors;
    }

    public void concat(ValidatorResult<T> validatorResult) {
        this.errors.addAll(validatorResult.errors);
    }
}
