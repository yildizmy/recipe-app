package com.github.yildizmy.dto.response;

import com.github.yildizmy.model.RecipeIngredient;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object for RecipeIngredient response
 */
@Data
public class RecipeIngredientResponse {

    private Long id;
    private String ingredientName;
    private BigDecimal amount;
    private String unitName;

    public RecipeIngredientResponse(RecipeIngredient recipeIngredient) {
        this.id = recipeIngredient.getIngredient().getId();
        this.ingredientName = recipeIngredient.getIngredient().getName();
        this.amount = recipeIngredient.getAmount();
        this.unitName = recipeIngredient.getUnit().getName();
    }
}
