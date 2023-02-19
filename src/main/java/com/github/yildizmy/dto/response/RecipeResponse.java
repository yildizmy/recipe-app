package com.github.yildizmy.dto.response;

import com.github.yildizmy.model.Recipe;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for Recipe response
 */
@Data
public class RecipeResponse {

    private Long id;
    private String title;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String instructions;
    private String difficulty;
    private String healthLabel;
    private CategoryResponse category;
    private List<RecipeIngredientResponse> ingredients;

    public RecipeResponse(Recipe recipe, CategoryResponse category, List<RecipeIngredientResponse> ingredients) {
        this.id = recipe.getId();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.prepTime = recipe.getPrepTime();
        this.cookTime = recipe.getCookTime();
        this.servings = recipe.getServings();
        this.instructions = recipe.getInstructions();
        this.difficulty = recipe.getDifficulty().getLabel();
        this.healthLabel = recipe.getHealthLabel().getLabel();
        this.category = category;
        this.ingredients = ingredients;
    }
}
