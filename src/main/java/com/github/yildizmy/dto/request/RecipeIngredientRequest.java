package com.github.yildizmy.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Data Transfer Object for RecipeIngredient request
 */
@Data
@NoArgsConstructor
public class RecipeIngredientRequest {

    Long recipeId;

    Long ingredientId;

    String ingredientName;

    @NotNull
    Long unitId;

    @NotNull
    BigDecimal amount;
}
