package com.github.yildizmy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.model.Ingredient;
import com.github.yildizmy.repository.IngredientRepository;
import com.github.yildizmy.service.IngredientService;
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

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {IngredientController.class, IngredientService.class})
@ExtendWith(SpringExtension.class)
class IngredientControllerTest {

    @MockBean
    private Clock clock;

    @Autowired
    private IngredientController ingredientController;

    @MockBean
    private IngredientRepository ingredientRepository;

    /**
     * Method under test: {@link IngredientController#findById(long)}
     */
    @Test
    void test_findById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(ingredientRepository.findById(any())).thenReturn(Optional.of(ingredient));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/ingredients/{id}", 123L);
        MockMvcBuilders.standaloneSetup(ingredientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123,\"name\":\"Ingredient\"}}"));
    }

    /**
     * Method under test: {@link IngredientController#create(IngredientRequest)}
     */
    @Test
    void test_create_when_IngredientNotExists() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(ingredientRepository.existsByNameIgnoreCase(any())).thenReturn(false);
        when(ingredientRepository.save(any())).thenReturn(ingredient);

        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setId(123L);
        ingredientRequest.setName("Ingredient");
        String content = (new ObjectMapper()).writeValueAsString(ingredientRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(ingredientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link IngredientController#findAll(SearchRequest)}
     */
    @Test
    void testFindAll() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();
        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");

        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(ingredient);
        PageImpl<Ingredient> pageImpl = new PageImpl<>(ingredientList);
        when(ingredientRepository.findAll((Specification<Ingredient>) any(), (Pageable) any())).thenReturn(pageImpl);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFilters(new ArrayList<>());
        searchRequest.setPage(1);
        searchRequest.setSize(3);
        searchRequest.setSorts(new ArrayList<>());

        String content = (new ObjectMapper()).writeValueAsString(searchRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(ingredientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link IngredientController#create(IngredientRequest)}
     */
    @Test
    void test_create_when_IngredientExists() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();
        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");
        ingredient.setRecipeIngredients(new HashSet<>());
        when(ingredientRepository.existsByNameIgnoreCase(any())).thenReturn(true);
        when(ingredientRepository.save(any())).thenReturn(ingredient);

        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setId(123L);
        ingredientRequest.setName("");

        String content = (new ObjectMapper()).writeValueAsString(ingredientRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(ingredientController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link IngredientController#deleteById(long)}
     */
    @Test
    void test_DeleteById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        doNothing().when(ingredientRepository).delete(any());
        when(ingredientRepository.findById(any())).thenReturn(Optional.of(ingredient));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/ingredients/{id}", 123L);
        MockMvcBuilders.standaloneSetup(ingredientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link IngredientController#update(IngredientRequest)}
     */
    @Test
    void test_Update() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(123L);
        ingredient.setName("Ingredient");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(123L);
        ingredient1.setName("Ingredient");
        ingredient1.setRecipeIngredients(new HashSet<>());

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(ingredientRepository.existsByNameIgnoreCase(any())).thenReturn(false);
        when(ingredientRepository.save(any())).thenReturn(ingredient1);
        when(ingredientRepository.findById(any())).thenReturn(Optional.of(ingredient));

        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setId(123L);
        ingredientRequest.setName("Ingredient");

        String content = (new ObjectMapper()).writeValueAsString(ingredientRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(ingredientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }
}

