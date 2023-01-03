package com.github.yildizmy.controller;

import com.github.yildizmy.common.Constants;
import com.github.yildizmy.dto.response.ApiResponse;
import com.github.yildizmy.dto.response.CommandResponse;
import lombok.RequiredArgsConstructor;
import com.github.yildizmy.dto.request.RecipeIngredientRequest;
import com.github.yildizmy.service.RecipeIngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RecipeIngredientController {

    private final Clock clock;
    private final RecipeIngredientService recipeIngredientService;

    /**
     * Adds ingredient to a recipe
     *
     * @return id of the recipe to which ingredient is added
     */
    @PostMapping("/recipeIngredients")
    public ResponseEntity<ApiResponse<CommandResponse>> addIngredientToRecipe(@Valid @RequestBody RecipeIngredientRequest request) {
        final CommandResponse response = recipeIngredientService.addIngredientToRecipe(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), Constants.SUCCESS, response));
    }

    /**
     * Removes ingredient from a recipe by recipeId and ingredientId
     *
     * @return id of the recipe from which ingredient is removed
     */
    @DeleteMapping("/recipeIngredients/recipes/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<ApiResponse<CommandResponse>> removeIngredientFromRecipe(@PathVariable long recipeId, @PathVariable long ingredientId) {
        final CommandResponse response = recipeIngredientService.removeIngredientFromRecipe(recipeId, ingredientId);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), Constants.SUCCESS, response));
    }
}
