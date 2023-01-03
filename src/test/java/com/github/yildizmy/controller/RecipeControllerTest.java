package com.github.yildizmy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.RecipeRequest;
import com.github.yildizmy.dto.request.RecipeSearchRequest;
import com.github.yildizmy.model.*;
import com.github.yildizmy.repository.CategoryRepository;
import com.github.yildizmy.repository.IngredientRepository;
import com.github.yildizmy.repository.RecipeRepository;
import com.github.yildizmy.repository.UnitRepository;
import com.github.yildizmy.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

@ContextConfiguration(classes = {RecipeController.class, RecipeService.class})
@ExtendWith(SpringExtension.class)
class RecipeControllerTest {

    @MockBean
    private Clock clock;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeController recipeController;

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private UnitRepository unitRepository;

    /**
     * Method under test: {@link RecipeController#findById(long)}
     */
    @Test
    void test_findById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

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

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/recipes/{id}", 123L);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123,\"title\":\"Title\"," +
                        "\"description\":\"Description\",\"prepTime\":1,\"cookTime\":1,\"servings\":1,\"instructions\":\"Instructions\",\"difficulty\":\"Easy\"," +
                        "\"healthLabel\":\"Default\",\"category\":{\"id\":123,\"name\":\"Category\",\"ordinal\":1},\"ingredients\":[]}}", false));
    }

    /**
     * Method under test: {@link RecipeController#findById(long)}
     */
    @Test
    void test_findById_WithRecipeIngredients() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("?");
        ingredient.setRecipeIngredients(new HashSet<>());

        Category category1 = new Category();
        category1.setId(123L);
        category1.setName("Category-1");
        category1.setOrdinal(1);
        category1.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category1);
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

        ArrayList<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        recipeIngredientList.add(recipeIngredient);

        Recipe recipe1 = new Recipe();
        recipe1.setCategory(category);
        recipe1.setCookTime(1);
        recipe1.setDescription("Description");
        recipe1.setDifficulty(Difficulty.EASY);
        recipe1.setHealthLabel(HealthLabel.DEFAULT);
        recipe1.setId(123L);
        recipe1.setInstructions("Instructions");
        recipe1.setPrepTime(1);
        recipe1.setRecipeIngredients(recipeIngredientList);
        recipe1.setServings(1);
        recipe1.setTitle("Title");

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/recipes/{id}", 123L);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123,\"title\":\"Title\",\"description\":\"Description\",\"prepTime\":1,\"cookTime\":1,\"servings\":1,\"instructions\":\"Instructions\","
                                        + "\"difficulty\":\"Easy\",\"healthLabel\":\"Default\",\"category\":{\"id\":123,\"name\":\"Category-1\",\"ordinal\":1},\"ingredients"
                                        + "\":[]}}"));
    }

    /**
     * Method under test: {@link RecipeController#create(RecipeRequest)}
     */
    @Test
    void test_create() throws Exception {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setCategoryId(123L);
        recipeRequest.setCookTime(1);
        recipeRequest.setDescription("Description");
        recipeRequest.setDifficulty(Difficulty.EASY);
        recipeRequest.setHealthLabel(HealthLabel.DEFAULT);
        recipeRequest.setId(123L);
        recipeRequest.setInstructions("Instructions");
        recipeRequest.setPrepTime(1);
        recipeRequest.setRecipeIngredients(new ArrayList<>());
        recipeRequest.setServings(1);
        recipeRequest.setTitle("?");

        String content = (new ObjectMapper()).writeValueAsString(recipeRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link RecipeController#findAll(SearchRequest)}
     */
    @Test
    void test_findAll() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category);
        recipe.setCookTime(100);
        recipe.setDescription("Description");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.DEFAULT);
        recipe.setId(123L);
        recipe.setInstructions("Instructions");
        recipe.setPrepTime(100);
        recipe.setRecipeIngredients(new ArrayList<>());
        recipe.setServings(100);
        recipe.setTitle("Title");

        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe);
        PageImpl<Recipe> pageImpl = new PageImpl<>(recipeList);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.findAll((Specification<Recipe>) any(), (Pageable) any())).thenReturn(pageImpl);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFilters(new ArrayList<>());
        searchRequest.setPage(1);
        searchRequest.setSize(3);
        searchRequest.setSorts(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(searchRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("{\"timestamp\":1640995200000,\"message\":\"Success\"," +
                        "\"data\":{\"content\":[{\"id\":123,\"title\":\"Title\",\"description\":\"Description\",\"prepTime\":100," +
                        "\"cookTime\":100,\"servings\":100,\"instructions\":\"Instructions\",\"difficulty\":\"Easy\",\"healthLabel\":\"Default\"," +
                        "\"category\":{\"id\":123,\"name\":\"Category\",\"ordinal\":1},\"ingredients\":[]}],\"pageable\":\"INSTANCE\"," +
                        "\"last\":true,\"totalElements\":1,\"totalPages\":1,\"size\":1,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false," +
                        "\"unsorted\":true},\"first\":true,\"numberOfElements\":1,\"empty\":false}}", false));
    }

    /**
     * Method under test: {@link RecipeController#findAll(SearchRequest)}
     */
    @Test
    void test_findAll_WithRecipeIngredients() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("?");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("?");
        ingredient.setRecipeIngredients(new HashSet<>());

        Category category1 = new Category();
        category1.setId(123L);
        category1.setName("?");
        category1.setOrdinal(1);
        category1.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category1);
        recipe.setCookTime(100);
        recipe.setDescription("Description");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.DEFAULT);
        recipe.setId(123L);
        recipe.setInstructions("?");
        recipe.setPrepTime(100);
        recipe.setRecipeIngredients(new ArrayList<>());
        recipe.setServings(100);
        recipe.setTitle("Title");

        RecipeIngredientId recipeIngredientId = new RecipeIngredientId();
        recipeIngredientId.setIngredientId(123L);
        recipeIngredientId.setRecipeId(123L);

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("?");
        unit.setRecipeIngredients(new HashSet<>());

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setAmount(BigDecimal.valueOf(42L));
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setRecipeIngredientId(recipeIngredientId);
        recipeIngredient.setUnit(unit);

        ArrayList<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        recipeIngredientList.add(recipeIngredient);

        Recipe recipe1 = new Recipe();
        recipe1.setCategory(category);
        recipe1.setCookTime(100);
        recipe1.setDescription("Description");
        recipe1.setDifficulty(Difficulty.EASY);
        recipe1.setHealthLabel(HealthLabel.DEFAULT);
        recipe1.setId(123L);
        recipe1.setInstructions("?");
        recipe1.setPrepTime(100);
        recipe1.setRecipeIngredients(recipeIngredientList);
        recipe1.setServings(100);
        recipe1.setTitle("Title");

        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        PageImpl<Recipe> pageImpl = new PageImpl<>(recipeList);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.findAll((Specification<Recipe>) any(), (Pageable) any())).thenReturn(pageImpl);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFilters(new ArrayList<>());
        searchRequest.setPage(1);
        searchRequest.setSize(3);
        searchRequest.setSorts(new ArrayList<>());
        String content = (new ObjectMapper()).writeValueAsString(searchRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"content\":[{\"id\":123,\"title\":\"Title\",\"description\":\"Description\",\"prepTime\":100,\"cookTime\":100,\"servings\":100,\"instructions\":\"?\",\"difficulty\":\"Easy\",\"healthLabel\":\"Default\",\"category\":{\"id\":123,\"name\":\"?\",\"ordinal\":1},\"ingredients\":[{\"id\":123,\"ingredientName\":\"?\",\"amount\":42,\"unitName\":\"?\"}]}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalElements\":1,\"totalPages\":1,\"size\":1,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":1,\"empty\":false}}", false));
    }

    /**
     * Method under test: {@link RecipeController#search(RecipeSearchRequest)}
     */
    @Test
    void test_search() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("?");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category);
        recipe.setCookTime(1);
        recipe.setDescription("Description");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setHealthLabel(HealthLabel.DEFAULT);
        recipe.setId(123L);
        recipe.setInstructions("?");
        recipe.setPrepTime(1);
        recipe.setRecipeIngredients(new ArrayList<>());
        recipe.setServings(1);
        recipe.setTitle("Title");

        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.search(any(), any(), any(), any(), any()))
                .thenReturn(recipeList);

        RecipeSearchRequest recipeSearchRequest = new RecipeSearchRequest();
        recipeSearchRequest.setIngredientEx("Ingredient Ex");
        recipeSearchRequest.setIngredientIn("Ingredient In");
        recipeSearchRequest.setIsVegetarian(true);
        recipeSearchRequest.setServings(1);
        recipeSearchRequest.setText("Text");

        String content = (new ObjectMapper()).writeValueAsString(recipeSearchRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/recipes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":[{\"id\":123,\"title\":\"Title\",\"description\":\"Description\",\"prepTime\":1,\"cookTime\":1,\"servings\":1,\"instructions\":\"?\",\"difficulty\":\"Easy"
                                        + "\",\"healthLabel\":\"Default\",\"category\":{\"id\":123,\"name\":\"?\",\"ordinal\":1},\"ingredients\":[]}]}"));
    }

    /**
     * Method under test: {@link RecipeController#search(RecipeSearchRequest)}
     */
    @Test
    void test_search_WithRecipeIngredientData() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");
        ingredient.setRecipeIngredients(new HashSet<>());

        Category category1 = new Category();
        category1.setId(123L);
        category1.setName("Category-1");
        category1.setOrdinal(1);
        category1.setRecipes(new HashSet<>());

        Recipe recipe = new Recipe();
        recipe.setCategory(category1);
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

        ArrayList<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        recipeIngredientList.add(recipeIngredient);

        Recipe recipe1 = new Recipe();
        recipe1.setCategory(category);
        recipe1.setCookTime(1);
        recipe1.setDescription("Description-1");
        recipe1.setDifficulty(Difficulty.EASY);
        recipe1.setHealthLabel(HealthLabel.DEFAULT);
        recipe1.setId(123L);
        recipe1.setInstructions("Instructions-1");
        recipe1.setPrepTime(1);
        recipe1.setRecipeIngredients(recipeIngredientList);
        recipe1.setServings(1);
        recipe1.setTitle("Title-1");

        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.search(any(), any(), any(), any(), any()))
                .thenReturn(recipeList);

        RecipeSearchRequest recipeSearchRequest = new RecipeSearchRequest();
        recipeSearchRequest.setIngredientEx("Ingredient Ex");
        recipeSearchRequest.setIngredientIn("Ingredient In");
        recipeSearchRequest.setIsVegetarian(true);
        recipeSearchRequest.setServings(1);
        recipeSearchRequest.setText("Text");

        String content = (new ObjectMapper()).writeValueAsString(recipeSearchRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/recipes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":[{\"id\":123,\"title\":\"Title-1\",\"description\":\"Description-1\",\"prepTime\":1,\"cookTime\":1,\"servings\":1,\"instructions\":\"Instructions-1\",\"difficulty\":\"Easy"
                                        + "\",\"healthLabel\":\"Default\",\"category\":{\"id\":123,\"name\":\"Category\",\"ordinal\":1},\"ingredients\":[{\"id\":123,"
                                        + "\"ingredientName\":\"Ingredient\",\"amount\":42,\"unitName\":\"Unit\"}]}]}"));
    }

    /**
     * Method under test: {@link RecipeController#create(RecipeRequest)}
     */
    @Test
    void test_create2() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Name");
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
        when(recipeRepository.save(any())).thenReturn(recipe);

        Category category1 = new Category();
        category1.setId(123L);
        category1.setName("Name");
        category1.setOrdinal(1);
        category1.setRecipes(new HashSet<>());
        Optional<Category> ofResult = Optional.of(category1);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(categoryRepository.findById(any())).thenReturn(ofResult);

        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setCategoryId(123L);
        recipeRequest.setCookTime(1);
        recipeRequest.setDescription("Description");
        recipeRequest.setDifficulty(Difficulty.EASY);
        recipeRequest.setHealthLabel(HealthLabel.DEFAULT);
        recipeRequest.setId(123L);
        recipeRequest.setInstructions("Instructions");
        recipeRequest.setPrepTime(1);
        recipeRequest.setRecipeIngredients(new ArrayList<>());
        recipeRequest.setServings(1);
        recipeRequest.setTitle("Title");

        String content = (new ObjectMapper()).writeValueAsString(recipeRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link RecipeController#deleteById(long)}
     */
    @Test
    void test_deleteById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Name");
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

        doNothing().when(recipeRepository).delete(any());
        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/recipes/{id}", 123L);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link RecipeController#update(RecipeRequest)}
     */
    @Test
    void test_update_WhenRecipeIsNotFound() throws Exception {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setCategoryId(123L);
        recipeRequest.setCookTime(1);
        recipeRequest.setDescription("Description");
        recipeRequest.setDifficulty(Difficulty.EASY);
        recipeRequest.setHealthLabel(HealthLabel.DEFAULT);
        recipeRequest.setId(123L);
        recipeRequest.setInstructions("Instructions");
        recipeRequest.setPrepTime(1);
        recipeRequest.setRecipeIngredients(new ArrayList<>());
        recipeRequest.setServings(1);
        recipeRequest.setTitle("?");
        String content = (new ObjectMapper()).writeValueAsString(recipeRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link RecipeController#update(RecipeRequest)}
     */
    @Test
    void test_update_WhenRecipeIsFound() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Name");
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

        Category category1 = new Category();
        category1.setId(123L);
        category1.setName("Name");
        category1.setOrdinal(1);

        Recipe recipe1 = new Recipe();
        recipe1.setCategory(category1);
        recipe1.setCookTime(1);
        recipe1.setDescription("Description");
        recipe1.setDifficulty(Difficulty.EASY);
        recipe1.setHealthLabel(HealthLabel.DEFAULT);
        recipe1.setId(123L);
        recipe1.setInstructions("Instructions");
        recipe1.setPrepTime(1);
        recipe1.setRecipeIngredients(new ArrayList<>());
        recipe1.setServings(1);
        recipe1.setTitle("Title");

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(recipeRepository.save(any())).thenReturn(recipe1);
        when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));

        Category category2 = new Category();
        category2.setId(123L);
        category2.setName("Name");
        category2.setOrdinal(1);
        category2.setRecipes(new HashSet<>());
        Optional<Category> ofResult1 = Optional.of(category2);
        when(categoryRepository.findById(any())).thenReturn(ofResult1);

        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setCategoryId(123L);
        recipeRequest.setCookTime(1);
        recipeRequest.setDescription("Description");
        recipeRequest.setDifficulty(Difficulty.EASY);
        recipeRequest.setHealthLabel(HealthLabel.DEFAULT);
        recipeRequest.setId(123L);
        recipeRequest.setInstructions("Instructions");
        recipeRequest.setPrepTime(1);
        recipeRequest.setRecipeIngredients(new ArrayList<>());
        recipeRequest.setServings(1);
        recipeRequest.setTitle("Title");
        String content = (new ObjectMapper()).writeValueAsString(recipeRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(recipeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }
}

