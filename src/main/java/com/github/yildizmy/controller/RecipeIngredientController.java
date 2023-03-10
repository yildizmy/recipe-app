package com.github.yildizmy.controller;

import com.github.yildizmy.dto.request.RecipeIngredientRequest;
import com.github.yildizmy.dto.response.ApiResponse;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.service.RecipeIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.Instant;

import static com.github.yildizmy.common.Constants.SUCCESS;

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
    public ResponseEntity<ApiResponse<CommandResponse>> addIngredientToRecipe(
            @Valid @RequestBody RecipeIngredientRequest request) {
        final CommandResponse response = recipeIngredientService.addIngredientToRecipe(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Removes ingredient from a recipe by recipeId and ingredientId
     *
     * @return id of the recipe from which ingredient is removed
     */
    @DeleteMapping("/recipeIngredients/recipes/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<ApiResponse<Void>> removeIngredientFromRecipe(
            @PathVariable long recipeId, @PathVariable long ingredientId) {
        recipeIngredientService.removeIngredientFromRecipe(recipeId, ingredientId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
