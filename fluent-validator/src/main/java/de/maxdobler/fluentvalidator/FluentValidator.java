package de.maxdobler.fluentvalidator;

public class FluentValidator<T, E> {
    private final FluentValidatorStep<T, E> firstStep;

    private FluentValidator(FluentValidatorStep<T, E> firstValidationStep) {
        this.firstStep = firstValidationStep;
    }

    public static <T, E> FluentValidator<T, E> beginWith(FluentValidatorStep<T, E> firstValidationStep) {
        return new FluentValidator<>(firstValidationStep);
    }

    public ValidatorResult runValidation(T value) {
        return firstStep.validate(value);
    }
}
