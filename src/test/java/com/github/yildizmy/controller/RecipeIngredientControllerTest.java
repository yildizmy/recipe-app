package com.github.yildizmy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yildizmy.dto.request.RecipeIngredientRequest;
import com.github.yildizmy.model.*;
import com.github.yildizmy.repository.IngredientRepository;
import com.github.yildizmy.repository.RecipeIngredientRepository;
import com.github.yildizmy.service.RecipeIngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RecipeIngredientController.class, RecipeIngredientService.class})
@ExtendWith(SpringExtension.class)
class RecipeIngredientControllerTest {

    @MockBean
    private Clock clock;

    @MockBean
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientController recipeIngredientController;

    @MockBean
    private RecipeIngredientRepository recipeIngredientRepository;

    /**
     * Method under test: {@link RecipeIngredientController#addIngredientToRecipe(RecipeIngredientRequest)} 
     */
    @Test
    void test_addIngredientForRecipe() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");
        ingredient.setRecipeIngredients(new HashSet<>());

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category);
        recipe.setCookTime(1);
        recipe.setDescription("Description");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.DEFAULT);
        recipe.setId(123L);
        recipe.setInstructions("Instructions");
        recipe.setPrepTime(1);
        recipe.setRecipeIngredients(new ArrayList<>());
        recipe.setServings(1);
        recipe.setTitle("Title");

        RecipeIngredientId recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setIngredientId(123L);
        recipeIngredientId.setRecipeId(123L);

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setAmount(BigDecimal.valueOf(42L));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setRecipeIngredientId(recipeIngredientId);
        recipeIngredient.setUnit(unit);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeIngredientRepository.existsByRecipeIdAndIngredientId((Long) any(), (Long) any())).thenReturn(false);
        when(recipeIngredientRepository.save(any())).thenReturn(recipeIngredient);

        RecipeIngredientRequest recipeIngredientRequest = new RecipeIngredientRequest();
        recipeIngredientRequest.setAmount(BigDecimal.valueOf(42L));
        recipeIngredientRequest.setIngredientId(123L);
        recipeIngredientRequest.setIngredientName("Ingredient Name");
        recipeIngredientRequest.setRecipeId(123L);
        recipeIngredientRequest.setUnitId(123L);

        String content = (new ObjectMapper()).writeValueAsString(recipeIngredientRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/recipeIngredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(recipeIngredientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link RecipeIngredientController#addIngredientToRecipe(RecipeIngredientRequest)} 
     */
    @Test
    void test_addIngredientForRecipe_2() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");
        ingredient.setRecipeIngredients(new HashSet<>());

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category);
        recipe.setCookTime(1);
        recipe.setDescription("Description");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.DEFAULT);
        recipe.setId(123L);
        recipe.setInstructions("Instructions");
        recipe.setPrepTime(1);
        recipe.setRecipeIngredients(new ArrayList<>());
        recipe.setServings(1);
        recipe.setTitle("Title");

        RecipeIngredientId recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setIngredientId(123L);
        recipeIngredientId.setRecipeId(123L);

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setAmount(BigDecimal.valueOf(42L));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setRecipeIngredientId(recipeIngredientId);
        recipeIngredient.setUnit(unit);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeIngredientRepository.existsByRecipeIdAndIngredientId((Long) any(), (Long) any())).thenReturn(true);
        when(recipeIngredientRepository.save(any())).thenReturn(recipeIngredient);

        RecipeIngredientRequest recipeIngredientRequest = new RecipeIngredientRequest();
        recipeIngredientRequest.setAmount(null);
        recipeIngredientRequest.setIngredientId(123L);
        recipeIngredientRequest.setIngredientName("Ingredient Name");
        recipeIngredientRequest.setRecipeId(123L);
        recipeIngredientRequest.setUnitId(123L);
        String content = (new ObjectMapper()).writeValueAsString(recipeIngredientRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/recipeIngredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(recipeIngredientController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link RecipeIngredientController
     */
    @Test
    void test_deleteById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();
        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");
        ingredient.setRecipeIngredients(new HashSet<>());

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category);
        recipe.setCookTime(1);
        recipe.setDescription("Description");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.DEFAULT);
        recipe.setId(123L);
        recipe.setInstructions("Instructions");
        recipe.setPrepTime(1);
        recipe.setRecipeIngredients(new ArrayList<>());
        recipe.setServings(1);
        recipe.setTitle("Title");

        RecipeIngredientId recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setIngredientId(123L);
        recipeIngredientId.setRecipeId(123L);

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setAmount(BigDecimal.valueOf(42L));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setRecipeIngredientId(recipeIngredientId);
        recipeIngredient.setUnit(unit);
        Optional<RecipeIngredient> ofResult = Optional.of(recipeIngredient);
        doNothing().when(recipeIngredientRepository).delete(any());
        when(recipeIngredientRepository.findByRecipeIdAndIngredientId((Long) any(), (Long) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/recipeIngredients/recipes/{recipeId}/ingredients/{ingredientId}", 123L, 123L);
        MockMvcBuilders.standaloneSetup(recipeIngredientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

