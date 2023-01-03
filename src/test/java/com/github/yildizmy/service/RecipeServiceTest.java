package com.github.yildizmy.service;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.RecipeIngredientRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Recipe Test for RecipeService methods
 */
@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @InjectMocks
    private RecipeService service;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private UnitRepository unitRepository;

    @Captor
    private ArgumentCaptor<Recipe> recipeCaptor;

    /**
     * Method under test: {@link RecipeService#findById(Long)}
     */
    @Test
    void findById_should_throw_NoSuchElementFoundException_when_RecipeIsNotFound() {
        when(recipeRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            service.findById(101L);
        });

        verify(recipeRepository).findById(101L);
    }

    /**
     * Method under test: {@link RecipeService#findById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/recipes.csv")
    void findById_should_return_RecipeResponse_when_RecipeIsFound(Long id, String title, String description, Integer prepTime,
                                                                  Integer cookTime, Integer servings, String instructions,
                                                                  String difficulty, String healthLabel) {
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);
        recipe.setInstructions(instructions);
        recipe.setDifficulty(Difficulty.valueOf(difficulty));
        recipe.setHealthLabel(HealthLabel.valueOf(healthLabel));
        recipe.setCategory(category);

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));

        RecipeResponse result = service.findById(id);

        assertEquals(id, result.getId());
        assertEquals(title, result.getTitle());
        assertEquals(description, result.getDescription());
        assertEquals(prepTime, result.getPrepTime());
        assertEquals(cookTime, result.getCookTime());
        assertEquals(servings, result.getServings());
        assertEquals(instructions, result.getInstructions());
        verify(recipeRepository).findById(id);
    }

    /**
     * Method under test: {@link RecipeService#findAll(SearchRequest)}
     */
    @Test
    void findAll_should_throw_NoSuchElementFoundException_when_NoRecipeIsFound() {
        SearchRequest request = new SearchRequest();

        when(recipeRepository.findAll((Specification<Recipe>) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> service.findAll(request));

        verify(recipeRepository).findAll((Specification<Recipe>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link RecipeService#findAll(SearchRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/recipes.csv")
    void findAll_should_return_RecipeResponseList_when_RecipeIsFound(Long id, String title, String description, Integer prepTime,
                                                                     Integer cookTime, Integer servings, String instructions,
                                                                     String difficulty, String healthLabel) {
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);
        recipe.setInstructions(instructions);
        recipe.setDifficulty(Difficulty.valueOf(difficulty));
        recipe.setHealthLabel(HealthLabel.valueOf(healthLabel));
        recipe.setCategory(category);

        List<Recipe> recipeList = Collections.singletonList(recipe);
        PageImpl<Recipe> pageImpl = new PageImpl<>(recipeList);

        when(recipeRepository.findAll((Specification<Recipe>) any(), (Pageable) any())).thenReturn(pageImpl);

        List<RecipeResponse> result = service.findAll(new SearchRequest()).toList();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(title, result.get(0).getTitle());
        assertEquals(description, result.get(0).getDescription());
        assertEquals(prepTime, result.get(0).getPrepTime());
        assertEquals(cookTime, result.get(0).getCookTime());
        assertEquals(servings, result.get(0).getServings());
        assertEquals(instructions, result.get(0).getInstructions());
        verify(recipeRepository).findAll((Specification<Recipe>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link RecipeService#search(RecipeSearchRequest)}
     */
    @Test
    void search_should_throw_NoSuchElementFoundException_when_NoRecipeIsFound() {
        List<Recipe> recipes = new ArrayList<>();
        RecipeSearchRequest request = new RecipeSearchRequest();

        when(recipeRepository.search(any(), any(), any(), any(), any())).thenReturn(recipes);

        assertThrows(NoSuchElementFoundException.class, () -> {
            service.search(request);
        });

        verify(recipeRepository).search(any(), any(), any(), any(), any());
    }

    /**
     * Method under test: {@link RecipeService#search(RecipeSearchRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/recipes.csv")
    void search_should_return_RecipeResponseList_when_RecipeIsFound(Long id, String title, String description, Integer prepTime,
                                                                    Integer cookTime, Integer servings, String instructions,
                                                                    String difficulty, String healthLabel) {
        boolean isVegetarian = true;
        String ingredientIn = "potatoes";
        String ingredientEx = "salmon";
        String text = "oven";
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);
        recipe.setInstructions(instructions);
        recipe.setDifficulty(Difficulty.valueOf(difficulty));
        recipe.setHealthLabel(HealthLabel.valueOf(healthLabel));
        recipe.setCategory(category);
        List<Recipe> recipes = Collections.singletonList(recipe);

        RecipeSearchRequest request = new RecipeSearchRequest();
        request.setIsVegetarian(isVegetarian);
        request.setServings(servings);
        request.setIngredientIn(ingredientIn);
        request.setIngredientEx(ingredientEx);
        request.setText(text);
        when(recipeRepository.search(isVegetarian, servings, ingredientIn, ingredientEx, text)).thenReturn(recipes);

        List<RecipeResponse> result = service.search(request);

        assertEquals(id, result.get(0).getId());
        assertEquals(title, result.get(0).getTitle());
        assertEquals(description, result.get(0).getDescription());
        assertEquals(prepTime, result.get(0).getPrepTime());
        assertEquals(cookTime, result.get(0).getCookTime());
        assertEquals(servings, result.get(0).getServings());
        assertEquals(instructions, result.get(0).getInstructions());
        verify(recipeRepository).search(any(), any(), any(), any(), any());
    }


    /**
     * Method under test: {@link RecipeService#create(RecipeRequest)}
     */
    @Test
    void create_should_throw_NoSuchElementFoundException_when_NoCategoryIsFound() {
        RecipeRequest request = new RecipeRequest();
        request.setCategoryId(201L);

        when(categoryRepository.findById(201L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.create(request));

        verify(ingredientRepository, never()).findById(any());
        verify(ingredientRepository, never()).existsByNameIgnoreCase(any());
        verify(ingredientRepository, never()).save(any());
        verify(unitRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RecipeService#create(RecipeRequest)}
     */
    @Test
    void create_should_throw_NoSuchElementFoundException_when_NoIngredientIsFound() {
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe();
        recipe.setId(101L);
        recipe.setTitle("Title");
        recipe.setDescription("Description");
        recipe.setPrepTime(30);
        recipe.setCookTime(45);
        recipe.setServings(6);
        recipe.setInstructions("Instructions");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.GLUTEN_FREE);
        recipe.setCategory(category);

        Ingredient ingredient = new Ingredient(401L, "Ingredient");
        RecipeIngredientRequest recipeIngredient = new RecipeIngredientRequest();
        recipeIngredient.setIngredientId(ingredient.getId());
        List<RecipeIngredientRequest> recipeIngredients = Collections.singletonList(recipeIngredient);

        RecipeRequest request = new RecipeRequest();
        request.setCategoryId(category.getId());
        request.setRecipeIngredients(recipeIngredients);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(ingredientRepository.findById(ingredient.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.create(request));

        verify(ingredientRepository, never()).existsByNameIgnoreCase(any());
        verify(ingredientRepository, never()).save(any());
        verify(unitRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any());
    }


    /**
     * Method under test: {@link RecipeService#create(RecipeRequest)}
     */
    @Test
    void create_should_throw_ElementAlreadyExistsException_when_IngredientExists() {
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe();
        recipe.setId(101L);
        recipe.setTitle("Title");
        recipe.setDescription("Description");
        recipe.setPrepTime(30);
        recipe.setCookTime(45);
        recipe.setServings(6);
        recipe.setInstructions("Instructions");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.GLUTEN_FREE);
        recipe.setCategory(category);

        RecipeIngredientRequest recipeIngredient = new RecipeIngredientRequest();
        recipeIngredient.setIngredientId(0L);
        recipeIngredient.setIngredientName("Ingredient");
        List<RecipeIngredientRequest> recipeIngredients = Collections.singletonList(recipeIngredient);

        RecipeRequest request = new RecipeRequest();
        request.setCategoryId(category.getId());
        request.setRecipeIngredients(recipeIngredients);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(ingredientRepository.existsByNameIgnoreCase("Ingredient")).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> service.create(request));

        verify(ingredientRepository, never()).save(any());
        verify(unitRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RecipeService#create(RecipeRequest)}
     */
    @Test
    void create_should_throw_NoSuchElementFoundException_when_UnitIsNotFound() {
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe();
        recipe.setId(101L);
        recipe.setTitle("Title");
        recipe.setDescription("Description");
        recipe.setPrepTime(30);
        recipe.setCookTime(45);
        recipe.setServings(6);
        recipe.setInstructions("Instructions");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.GLUTEN_FREE);
        recipe.setCategory(category);

        Unit unit = new Unit(301L, "Unit");

        RecipeIngredientRequest recipeIngredient = new RecipeIngredientRequest();
        recipeIngredient.setIngredientId(0L);
        recipeIngredient.setIngredientName("Ingredient");
        List<RecipeIngredientRequest> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(recipeIngredient);
        recipeIngredient.setUnitId(unit.getId());

        RecipeRequest request = new RecipeRequest();
        request.setCategoryId(category.getId());
        request.setRecipeIngredients(recipeIngredients);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(ingredientRepository.existsByNameIgnoreCase("Ingredient")).thenReturn(false);
        when(unitRepository.findById(unit.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.create(request));

        verify(recipeRepository, never()).save(any());
    }


    /**
     * Method under test: {@link RecipeService#create(RecipeRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/recipes.csv")
    void create_should_returnCommandResponseWithGivenIngredient_when_IngredientIsFound(Long id, String title, String description, Integer prepTime,
                                                                                       Integer cookTime, Integer servings, String instructions,
                                                                                       String difficulty, String healthLabel) {
        Category category = new Category(201L, "Category", 1);
        Unit unit = new Unit(301L, "Unit");
        Ingredient ingredient = new Ingredient(401L, "Ingredient");

        RecipeIngredientRequest recipeIngredient = new RecipeIngredientRequest();
        recipeIngredient.setAmount(BigDecimal.valueOf(250));
        recipeIngredient.setUnitId(unit.getId());
        recipeIngredient.setIngredientId(ingredient.getId());
        List<RecipeIngredientRequest> recipeIngredients = Collections.singletonList(recipeIngredient);

        RecipeRequest request = new RecipeRequest();
        request.setId(id);
        request.setTitle(title);
        request.setDescription(description);
        request.setPrepTime(prepTime);
        request.setCookTime(cookTime);
        request.setServings(servings);
        request.setInstructions(instructions);
        request.setDifficulty(Difficulty.valueOf(difficulty));
        request.setHealthLabel(HealthLabel.valueOf(healthLabel));
        request.setCategoryId(category.getId());
        request.setRecipeIngredients(recipeIngredients);


        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(ingredientRepository.findById(ingredient.getId())).thenReturn(Optional.of(ingredient));
        when(unitRepository.findById(unit.getId())).thenReturn(Optional.of(unit));

        verify(ingredientRepository, never()).existsByNameIgnoreCase(any());

        CommandResponse result = service.create(request);
        verify(recipeRepository).save(recipeCaptor.capture());
        Recipe capturedRecipe = recipeCaptor.getValue();

        assertEquals(id, result.getId());
        assertEquals(capitalizeFully(title), capturedRecipe.getTitle());
        assertEquals(description, capturedRecipe.getDescription());
        assertEquals(prepTime, capturedRecipe.getPrepTime());
        assertEquals(cookTime, capturedRecipe.getCookTime());
        assertEquals(servings, capturedRecipe.getServings());
        assertEquals(instructions, capturedRecipe.getInstructions());
        verify(recipeRepository).save(capturedRecipe);
        verify(ingredientRepository, never()).save(any());
    }


    /**
     * Method under test: {@link RecipeService#update(RecipeRequest)}
     */
    @Test
    void update_should_throw_NoSuchElementFoundException_when_RecipeNotFound() {
        Recipe recipe = new Recipe();
        RecipeRequest request = new RecipeRequest();

        when(recipeRepository.findById(request.getId())).thenReturn(Optional.of(recipe));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.update(request));

        verify(recipeRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RecipeService#update(RecipeRequest)}
     */
    @Test
    void update_should_throw_NoSuchElementFoundException_when_CategoryNotFound() {
        RecipeRequest request = new RecipeRequest();

        when(recipeRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.update(request));

        verify(categoryRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RecipeService#update(RecipeRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/recipes.csv")
    void update_should_return_CommandResponse_when_RecipeNotExists(Long id, String title, String description, Integer prepTime,
                                                                   Integer cookTime, Integer servings, String instructions,
                                                                   String difficulty, String healthLabel) {
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe(id);
        RecipeRequest request = new RecipeRequest();
        request.setId(id);
        request.setTitle(title);
        request.setDescription(description);
        request.setPrepTime(prepTime);
        request.setCookTime(cookTime);
        request.setServings(servings);
        request.setInstructions(instructions);
        request.setDifficulty(Difficulty.valueOf(difficulty));
        request.setHealthLabel(HealthLabel.valueOf(healthLabel));
        request.setCategoryId(category.getId());

        when(recipeRepository.findById(request.getId())).thenReturn(Optional.of(recipe));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

        service.update(request);
        verify(recipeRepository).save(recipeCaptor.capture());
        Recipe capturedRecipe = recipeCaptor.getValue();

        assertEquals(id, capturedRecipe.getId());
        assertEquals(capitalizeFully(title), capturedRecipe.getTitle());
        assertEquals(description, capturedRecipe.getDescription());
        assertEquals(prepTime, capturedRecipe.getPrepTime());
        assertEquals(cookTime, capturedRecipe.getCookTime());
        assertEquals(servings, capturedRecipe.getServings());
        assertEquals(instructions, capturedRecipe.getInstructions());
        verify(recipeRepository).save(capturedRecipe);
    }


    /**
     * Method under test: {@link RecipeService#deleteById(Long)}
     */
    @Test
    void deleteById_should_throw_NoSuchElementFoundException_when_RecipeNotExists() {
        when(recipeRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.deleteById(101L));

        verify(recipeRepository, never()).delete(any());
    }

    /**
     * Method under test: {@link RecipeService#deleteById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/recipes.csv")
    void deleteById_should_return_CommandResponse_when_RecipeIsFound(Long id, String title, String description, Integer prepTime,
                                                                     Integer cookTime, Integer servings, String instructions,
                                                                     String difficulty, String healthLabel) {
        Category category = new Category(201L, "Category", 1);
        Recipe recipe = new Recipe(id);
        recipe.setId(id);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);
        recipe.setInstructions(instructions);
        recipe.setDifficulty(Difficulty.valueOf(difficulty));
        recipe.setHealthLabel(HealthLabel.valueOf(healthLabel));
        recipe.setCategory(category);

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));

        service.deleteById(id);
        verify(recipeRepository).delete(recipeCaptor.capture());
        Recipe capturedRecipe = recipeCaptor.getValue();

        assertEquals(id, capturedRecipe.getId());
        assertEquals(title, capturedRecipe.getTitle());
        assertEquals(description, capturedRecipe.getDescription());
        assertEquals(prepTime, capturedRecipe.getPrepTime());
        assertEquals(cookTime, capturedRecipe.getCookTime());
        assertEquals(servings, capturedRecipe.getServings());
        assertEquals(instructions, capturedRecipe.getInstructions());
        verify(recipeRepository).delete(capturedRecipe);
    }
}
