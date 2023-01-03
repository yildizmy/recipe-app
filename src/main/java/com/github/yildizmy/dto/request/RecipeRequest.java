package com.github.yildizmy.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.yildizmy.model.Difficulty;
import com.github.yildizmy.model.HealthLabel;

import javax.validation.constraints.*;
import java.util.List;

/**
 * Data Transfer Object for Recipe request
 */
@Data
@NoArgsConstructor
public class RecipeRequest {

    Long id;

    Long categoryId;

    @NotBlank
    @Size(min = 3, max = 50)
    String title;

    String description;

    Integer prepTime;

    Integer cookTime;

    @Min(1)
    @Max(12)
    Integer servings;

    @NotBlank
    String instructions;

    @NotNull
    Difficulty difficulty;

    @NotNull
    HealthLabel healthLabel;

    List<RecipeIngredientRequest> recipeIngredients;
}
