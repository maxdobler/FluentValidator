package de.maxdobler.fluentvalidator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static de.maxdobler.fluentvalidator.FluentValidatorStep.withValidator;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FluentValidatorTest {

    private static final String STRING_IS_EMPTY = "String is empty.";
    private static final String STRING_DOES_NOT_CONTAIN_ABC = "String does not contain 'abc'.";
    private static final String STRING_DOES_NOT_CONTAIN_XYZ = "String does not contain 'xyz'.";

    private Validator<String> notEmptyValidator = string -> string == null || string.isEmpty()
            ? ValidatorResult.error(STRING_IS_EMPTY)
            : ValidatorResult.valid();
    private Validator<String> containsAbcValidator = string -> !string.contains("abc")
            ? ValidatorResult.error(STRING_DOES_NOT_CONTAIN_ABC)
            : ValidatorResult.valid();
    private Validator<String> containsXyzValidator = string -> !string.contains("xyz")
            ? ValidatorResult.error(STRING_DOES_NOT_CONTAIN_XYZ)
            : ValidatorResult.valid();

    @Test
    void oneValidator_Invalid() {
        ValidatorResult validatorResult = FluentValidator //
                .beginWith(withValidator(notEmptyValidator))
                .runValidation("");

        assertErrors(validatorResult, singletonList(STRING_IS_EMPTY));
    }

    @Test
    void oneValidator() {
        ValidatorResult validatorResult = FluentValidator //
                .beginWith(withValidator(notEmptyValidator))
                .runValidation("abc");

        assertValid(validatorResult);
    }

    @Test
    void collectErrorsFromValidators() {
        ValidatorResult validatorResult = FluentValidator //
                .beginWith(withValidator(notEmptyValidator)
                        .andThen(withValidator(containsAbcValidator)))
                .runValidation("");

        assertErrors(validatorResult, asList(STRING_IS_EMPTY, STRING_DOES_NOT_CONTAIN_ABC));
    }

    @Test
    void runValidatorsRecursiv() {
        ValidatorResult validatorResult = FluentValidator //
                .beginWith(withValidator(notEmptyValidator)
                        .andThen(withValidator(containsAbcValidator)
                                .andThen(withValidator(containsXyzValidator))))
                .runValidation("");

        assertErrors(validatorResult, asList(STRING_IS_EMPTY, STRING_DOES_NOT_CONTAIN_ABC, STRING_DOES_NOT_CONTAIN_XYZ));
    }


    //TODO: Better naming
    @Test
    void multipleAndThenValidators() {
        ValidatorResult validatorResult = FluentValidator //
                .beginWith(withValidator(notEmptyValidator)
                        .andThen(withValidator(containsAbcValidator))
                        .andThen(withValidator(containsXyzValidator)))
                .runValidation("");

        assertErrors(validatorResult, asList(STRING_IS_EMPTY, STRING_DOES_NOT_CONTAIN_ABC, STRING_DOES_NOT_CONTAIN_XYZ));
    }

    @Test
    void onlyContinueValidationIfSuccessful() {
        ValidatorResult validatorResult = FluentValidator //
                .beginWith(withValidator(notEmptyValidator)
                        .ifSuccessfulThen(withValidator(containsAbcValidator))
                        .andThen(withValidator(containsXyzValidator)))
                .runValidation("");

        assertErrors(validatorResult, asList(STRING_IS_EMPTY, STRING_DOES_NOT_CONTAIN_XYZ));
    }

    @Test
    void validateSuccessfulValidatorFirst() {
        ValidatorResult validatorResult = FluentValidator //
                .beginWith(withValidator(notEmptyValidator)
                        .ifSuccessfulThen(withValidator(containsAbcValidator))
                        .andThen(withValidator(containsXyzValidator)))
                .runValidation("a");

        assertErrors(validatorResult, asList(STRING_DOES_NOT_CONTAIN_ABC, STRING_DOES_NOT_CONTAIN_XYZ));
    }


    private void assertValid(ValidatorResult validatorResult) {
        assertTrue(validatorResult.isValid());
        assertIterableEquals(emptyList(), validatorResult.getErrors());
    }

    private void assertErrors(ValidatorResult validatorResult, List<String> strings) {
        assertFalse(validatorResult.isValid());
        assertIterableEquals(strings, validatorResult.getErrors());
    }
}