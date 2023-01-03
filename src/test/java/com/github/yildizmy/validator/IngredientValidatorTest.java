package com.github.yildizmy.validator;

import com.github.yildizmy.dto.request.RecipeRequest;
import com.github.yildizmy.dto.request.RecipeIngredientRequest;
import com.github.yildizmy.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Recipe Test for IngredientValidator methods
 */
@ExtendWith(MockitoExtension.class)
class IngredientValidatorTest {

    @InjectMocks
    private IngredientValidator ingredientValidator;

    @Mock
    private StringUtils stringUtils;

    /**
     * Method under test: {@link IngredientValidator#isValid(RecipeRequest, ConstraintValidatorContext)}
     */
    @Test
    void test_IsValid() {
        List<Long> ingredientIdList = List.of(201L, 202L);

        RecipeIngredientRequest recipeIngredient1 = new RecipeIngredientRequest();
        recipeIngredient1.setRecipeId(101L);
        recipeIngredient1.setIngredientId(201L);

        RecipeIngredientRequest recipeIngredient2 = new RecipeIngredientRequest();
        recipeIngredient2.setRecipeId(102L);
        recipeIngredient2.setIngredientId(202L);

        List<RecipeIngredientRequest> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(recipeIngredient1);
        recipeIngredients.add(recipeIngredient2);

        RecipeRequest request = new RecipeRequest();
        request.setRecipeIngredients(recipeIngredients);

        when(stringUtils.areAllUnique(ingredientIdList)).thenReturn(true);

        boolean result = ingredientValidator.isValid(request, any());

        assertTrue(result);
    }
}
