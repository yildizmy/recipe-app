package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.model.RecipeIngredient;
import com.github.yildizmy.model.Unit;
import com.github.yildizmy.model.Ingredient;
import com.github.yildizmy.model.Recipe;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeIngredientRequestMapperTest {

    @Test
    void mapToEntity_should_return_RecipeIngredientEntity() {

        Recipe recipe = new Recipe(101L);
        Ingredient ingredient = new Ingredient(201L);
        Unit unit = new Unit(301L);
        BigDecimal amount = BigDecimal.valueOf(250);

        RecipeIngredient result = RecipeIngredientRequestMapper.mapToEntity(recipe, ingredient, unit, amount);

        assertEquals(recipe.getId(), result.getRecipe().getId());
        assertEquals(ingredient.getId(), result.getIngredient().getId());
        assertEquals(unit.getId(), result.getUnit().getId());
        assertEquals(amount, result.getAmount());
    }
}
