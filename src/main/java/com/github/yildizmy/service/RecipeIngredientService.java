package com.github.yildizmy.service;

import com.github.yildizmy.common.Constants;
import com.github.yildizmy.dto.request.RecipeIngredientRequest;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.model.Ingredient;
import com.github.yildizmy.model.RecipeIngredient;
import com.github.yildizmy.model.Unit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.model.Recipe;
import com.github.yildizmy.repository.IngredientRepository;
import com.github.yildizmy.repository.RecipeIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service used for adding and removing recipeIngredient
 */
@Slf4j(topic = "RecipeIngredientService")
@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * Adds ingredient to the given recipe
     *
     * @param request
     * @return
     */
    @Transactional
    public CommandResponse addIngredientToRecipe(RecipeIngredientRequest request) {
        final Ingredient ingredient;
        if (request.getIngredientId() != 0) {
            // check if the ingredient is already defined for the recipe
            if (recipeIngredientRepository.existsByRecipeIdAndIngredientId(request.getRecipeId(), request.getIngredientId())) {
                log.error(Constants.ALREADY_EXISTS_INGREDIENT);
                throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_INGREDIENT + ". IngredientId: " + request.getIngredientId());
            }
            ingredient = new Ingredient(request.getIngredientId());
        } else {
            // check if the new ingredient is already defined before
            if (ingredientRepository.existsByNameIgnoreCase(request.getIngredientName())) {
                log.error(Constants.ALREADY_EXISTS_INGREDIENT);
                throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_INGREDIENT + ". IngredientName: " + request.getIngredientName());
            }
            ingredient = ingredientRepository.save(new Ingredient(0L, request.getIngredientName()));
        }
        // if needed, we can also check if recipe and unit values exists in db (we assumed recipe is already defined and unit is selected from the list)
        final Recipe recipe = new Recipe(request.getRecipeId());
        final Unit unit = new Unit(request.getUnitId());
        final RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, ingredient, unit, request.getAmount());

        recipeIngredientRepository.save(recipeIngredient);
        return CommandResponse.builder().id(recipeIngredient.getRecipe().getId()).build();
    }

    /**
     * Removes ingredient from the given recipe
     *
     * @param recipeId
     * @param ingredientId
     * @return
     */
    public CommandResponse removeIngredientFromRecipe(Long recipeId, Long ingredientId) {
        final RecipeIngredient recipeIngredient = recipeIngredientRepository.findByRecipeIdAndIngredientId(recipeId, ingredientId)
                .orElseThrow(() -> {
                    log.error(Constants.NOT_FOUND_INGREDIENT);
                    return new NoSuchElementFoundException(Constants.NOT_FOUND_INGREDIENT);
                });
        recipeIngredientRepository.delete(recipeIngredient);
        return CommandResponse.builder().id(recipeId).build();
    }
}
