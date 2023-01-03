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

    private Long id;

    private Long categoryId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String title;

    private String description;

    private Integer prepTime;

    private Integer cookTime;

    @Min(1)
    @Max(12)
    private Integer servings;

    @NotBlank
    private String instructions;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private HealthLabel healthLabel;

    private List<RecipeIngredientRequest> recipeIngredients;
}
