package com.github.yildizmy.service;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.common.filter.SearchSpecification;
import com.github.yildizmy.dto.mapper.RecipeRequestMapper;
import com.github.yildizmy.dto.request.RecipeRequest;
import com.github.yildizmy.dto.request.RecipeSearchRequest;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.dto.response.RecipeResponse;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.model.*;
import com.github.yildizmy.repository.CategoryRepository;
import com.github.yildizmy.repository.IngredientRepository;
import com.github.yildizmy.repository.RecipeRepository;
import com.github.yildizmy.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.yildizmy.common.Constants.*;
import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Service used for adding, updating, removing and fetching recipes
 */
@Slf4j(topic = "RecipeService")
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;

    /**
     * Fetches a recipe by the given id
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public RecipeResponse findById(Long id) {
        return recipeRepository.findById(id)
                .map(RecipeResponse::new)
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_RECIPE);
                    return new NoSuchElementFoundException(NOT_FOUND_RECIPE);
                });
    }

    /**
     * Fetches all recipes based on the given recipe filter parameters
     *
     * @param request
     * @return Paginated recipe data
     */
    @Transactional(readOnly = true)
    public Page<RecipeResponse> findAll(SearchRequest request) {
        final SearchSpecification<Recipe> specification = new SearchSpecification<>(request);
        final Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        final Page<RecipeResponse> recipes = recipeRepository.findAll(specification, pageable)
                .map(RecipeResponse::new);
        if (recipes.isEmpty()) {
            log.error(NOT_FOUND_RECORD);
            throw new NoSuchElementFoundException(NOT_FOUND_RECORD);
        }
        return recipes;
    }

    /**
     * Search recipes based on the given recipe and ingredient parameters
     *
     * @param request
     * @return Recipe list
     */
    @Transactional(readOnly = true)
    public List<RecipeResponse> search(RecipeSearchRequest request) {
        final List<RecipeResponse> recipes = recipeRepository.search(
                        request.getIsVegetarian(),
                        request.getServings(),
                        request.getIngredientIn(),
                        request.getIngredientEx(),
                        request.getText()).stream()
                .map(RecipeResponse::new).toList();
        if (recipes.isEmpty()) {
            log.error(NOT_FOUND_RECORD);
            throw new NoSuchElementFoundException(NOT_FOUND_RECORD);
        }
        return recipes;
    }

    /**
     * Creates a new recipe and ingredients belonging to the recipe using the given request parameters
     *
     * @param request
     * @return
     */
    @Transactional
    public CommandResponse create(RecipeRequest request) {
        final Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_CATEGORY);
                    return new NoSuchElementFoundException(NOT_FOUND_CATEGORY);
                });
        final Recipe recipe = RecipeRequestMapper.mapToEntity(request);
        recipe.setCategory(category);

        request.getRecipeIngredients().stream()
                .forEach(recipeIngredient -> {
                    final Ingredient ingredient;
                    if (recipeIngredient.getIngredientId() != 0) {
                        ingredient = ingredientRepository.findById(recipeIngredient.getIngredientId())
                                .orElseThrow(() -> {
                                    log.error(NOT_FOUND_INGREDIENT);
                                    return new NoSuchElementFoundException(NOT_FOUND_INGREDIENT);
                                });
                    } else {
                        // check if the new ingredient is already defined before
                        if (ingredientRepository.existsByNameIgnoreCase(recipeIngredient.getIngredientName())) {
                            log.error(String.format(ALREADY_EXISTS_INGREDIENT, recipeIngredient.getIngredientId()));
                            throw new ElementAlreadyExistsException(String.format(ALREADY_EXISTS_INGREDIENT, recipeIngredient.getIngredientId()));
                        }
                        ingredient = ingredientRepository.save(new Ingredient(0L, recipeIngredient.getIngredientName()));
                    }
                    final Unit unit = unitRepository.findById(recipeIngredient.getUnitId())
                            .orElseThrow(() -> {
                                log.error(NOT_FOUND_UNIT);
                                return new NoSuchElementFoundException(NOT_FOUND_UNIT);
                            });
                    recipe.addRecipeIngredient(new RecipeIngredient(recipe, ingredient, unit, recipeIngredient.getAmount()));
                });
        recipeRepository.save(recipe);
        return CommandResponse.builder().id(recipe.getId()).build();
    }

    /**
     * Updates recipe using the given request parameters
     *
     * @param request
     * @return
     */
    @Transactional
    // for adding/removing ingredients for a current recipe, use RecipeIngredientService methods
    public CommandResponse update(RecipeRequest request) {
        final Recipe recipe = recipeRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_RECIPE);
                    return new NoSuchElementFoundException(NOT_FOUND_RECIPE);
                });

        final Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_CATEGORY);
                    return new NoSuchElementFoundException(NOT_FOUND_CATEGORY);
                });

        recipe.setTitle(capitalizeFully(request.getTitle()));
        recipe.setDescription(request.getDescription());
        recipe.setPrepTime(request.getPrepTime());
        recipe.setCookTime(request.getCookTime());
        recipe.setServings(request.getServings());
        recipe.setInstructions(request.getInstructions());
        recipe.setDifficulty(request.getDifficulty());
        recipe.setHealthLabel(request.getHealthLabel());
        recipe.setCategory(category);
        recipeRepository.save(recipe);
        return CommandResponse.builder().id(recipe.getId()).build();
    }

    /**
     * Deletes recipe by the given id
     *
     * @param id
     * @return
     */
    public CommandResponse deleteById(Long id) {
        final Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_RECIPE);
                    return new NoSuchElementFoundException(NOT_FOUND_RECIPE);
                });
        recipeRepository.delete(recipe);
        return CommandResponse.builder().id(id).build();
    }
}
