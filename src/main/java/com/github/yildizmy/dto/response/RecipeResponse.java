package com.github.yildizmy.dto.response;

import lombok.Data;
import com.github.yildizmy.model.Recipe;

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
    List<RecipeIngredientResponse> ingredients;

    public RecipeResponse(Recipe recipe) {
        this.id = recipe.getId();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.prepTime = recipe.getPrepTime();
        this.cookTime = recipe.getCookTime();
        this.servings = recipe.getServings();
        this.instructions = recipe.getInstructions();
        this.difficulty = recipe.getDifficulty().getLabel();
        this.healthLabel = recipe.getHealthLabel().getLabel();
        this.category = new CategoryResponse(recipe.getCategory());
        this.ingredients = recipe.getRecipeIngredients().stream().map(RecipeIngredientResponse::new).toList();
    }
}
