package com.github.yildizmy.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.github.yildizmy.common.Constants.NOT_VALIDATED_ELEMENT;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom constraint annotation for IngredientValidator
 */
@Documented
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {IngredientValidator.class})
public @interface ValidIngredient {

    String message() default NOT_VALIDATED_ELEMENT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}