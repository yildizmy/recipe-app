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

    private Long recipeId;

    private Long ingredientId;

    private String ingredientName;

    @NotNull
    private Long unitId;

    @NotNull
    private BigDecimal amount;
}
