package de.maxdobler.fluentvalidator;

public class FluentValidator<T> {
    private final FluentValidatorStep<T> firstStep;

    private FluentValidator(FluentValidatorStep<T> firstValidationStep) {
        this.firstStep = firstValidationStep;
    }

    public static <T> FluentValidator<T> beginWith(FluentValidatorStep<T> firstValidationStep) {
        return new FluentValidator<T>(firstValidationStep);
    }

    public ValidatorResult runValidation(T value) {
        return firstStep.validate(value);
    }
}
