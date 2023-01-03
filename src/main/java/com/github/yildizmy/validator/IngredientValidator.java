package com.github.yildizmy.validator;

import com.github.yildizmy.dto.request.RecipeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.yildizmy.dto.request.RecipeIngredientRequest;
import com.github.yildizmy.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates the ingredients in the RecipeRequest. If the request contains duplicate ingredients returns error
 */
@Slf4j(topic = "IngredientValidator")
@RequiredArgsConstructor
public class IngredientValidator implements ConstraintValidator<ValidIngredient, RecipeRequest> {

    private final StringUtils stringUtils;

    @Override
    public void initialize(ValidIngredient constraintAnnotation) {
    }

    @Override
    public boolean isValid(RecipeRequest request, ConstraintValidatorContext context) {
        return stringUtils.areAllUnique(request.getRecipeIngredients().stream().map(RecipeIngredientRequest::getIngredientId).toList());
    }
}
