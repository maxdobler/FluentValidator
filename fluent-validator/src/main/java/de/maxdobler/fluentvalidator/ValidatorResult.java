package de.maxdobler.fluentvalidator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorResult {
    private List<String> errors;

    public ValidatorResult(String error) {
        this();
        this.errors.add(error);
    }

    public ValidatorResult() {
        this.errors = new ArrayList<>();
    }

    public static ValidatorResult valid() {
        return new ValidatorResult();
    }

    public static ValidatorResult error(String error) {
        return new ValidatorResult(error);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void concat(ValidatorResult validatorResult) {
        this.errors.addAll(validatorResult.errors);
    }
}
