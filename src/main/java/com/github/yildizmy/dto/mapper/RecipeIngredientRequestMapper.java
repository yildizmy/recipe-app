package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.model.Ingredient;
import com.github.yildizmy.model.Recipe;
import com.github.yildizmy.model.RecipeIngredient;
import com.github.yildizmy.model.Unit;

import java.math.BigDecimal;

/**
 * Mapper for RecipeIngredientRequest
 */
public class RecipeIngredientRequestMapper {

    private RecipeIngredientRequestMapper() {
    }

    /**
     * Maps RecipeIngredientRequest fields to a new RecipeIngredient
     *
     * @param recipe
     * @param ingredient
     * @param unit
     * @param amount
     * @return RecipeIngredient model
     */
    public static RecipeIngredient mapToEntity(Recipe recipe, Ingredient ingredient, Unit unit, BigDecimal amount) {
        return new RecipeIngredient(
                recipe,
                ingredient,
                unit,
                amount
        );
    }
}
