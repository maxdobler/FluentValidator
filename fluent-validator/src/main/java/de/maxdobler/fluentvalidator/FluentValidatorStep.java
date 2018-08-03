package de.maxdobler.fluentvalidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FluentValidatorStep<T> {
    private final Validator<T> validator;
    private List<FluentValidatorStep<T>> nextValidator = new ArrayList<>();
    private Optional<FluentValidatorStep<T>> successValidator = Optional.empty();

    private FluentValidatorStep(Validator<T> validator) {
        this.validator = validator;
    }

    public static <T> FluentValidatorStep<T> withValidator(Validator<T> validator) {
        return new FluentValidatorStep<>(validator);
    }

    public ValidatorResult validate(T value) {
        ValidatorResult validatorResult = validator.validate(value);

        if (validatorResult.isValid()) {
            successValidator.map(step -> step.validate(value))
                    .ifPresent(validatorResult::concat);
        }

        nextValidator.stream()
                .map(step -> step.validate(value))
                .forEach(validatorResult::concat);

        return validatorResult;
    }

    public FluentValidatorStep<T> andThen(FluentValidatorStep<T> nextValidator) {
        this.nextValidator.add(nextValidator);
        return this;
    }

    public FluentValidatorStep<T> ifSuccessfulThen(FluentValidatorStep<T> validator) {
        this.successValidator = Optional.ofNullable(validator);
        return this;
    }
}
