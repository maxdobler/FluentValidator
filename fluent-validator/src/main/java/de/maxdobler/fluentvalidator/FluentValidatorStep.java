package de.maxdobler.fluentvalidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FluentValidatorStep<T, E> {
    private final Validator<T, E> validator;
    private List<FluentValidatorStep<T, E>> nextValidator = new ArrayList<>();
    private Optional<FluentValidatorStep<T, E>> successValidator = Optional.empty();

    private FluentValidatorStep(Validator<T, E> validator) {
        this.validator = validator;
    }

    public static <T, E> FluentValidatorStep<T, E> withValidator(Validator<T, E> validator) {
        return new FluentValidatorStep<>(validator);
    }

    public ValidatorResult<E> validate(T value) {
        ValidatorResult<E> validatorResult = validator.validate(value);

        if (validatorResult.isValid()) {
            successValidator.map(step -> step.validate(value))
                    .ifPresent(validatorResult::concat);
        }

        nextValidator.stream()
                .map(step -> step.validate(value))
                .forEach(validatorResult::concat);

        return validatorResult;
    }

    public FluentValidatorStep<T, E> andThen(FluentValidatorStep<T, E> nextValidator) {
        this.nextValidator.add(nextValidator);
        return this;
    }

    public FluentValidatorStep<T, E> ifSuccessfulThen(FluentValidatorStep<T, E> validator) {
        this.successValidator = Optional.ofNullable(validator);
        return this;
    }
}
