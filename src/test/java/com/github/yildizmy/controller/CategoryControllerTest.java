package com.github.yildizmy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yildizmy.model.Category;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.CategoryRequest;
import com.github.yildizmy.repository.CategoryRepository;
import com.github.yildizmy.service.CategoryService;
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

@ContextConfiguration(classes = {CategoryController.class, CategoryService.class})
@ExtendWith(SpringExtension.class)
class CategoryControllerTest {

    @MockBean
    private Clock clock;

    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryRepository categoryRepository;

    /**
     * Method under test: {@link CategoryController#findById(long)}
     */
    @Test
    void test_findById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/categories/{id}", 123L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123,\"name\":\"Category\",\"ordinal\":1}}"));
    }

    /**
     * Method under test: {@link CategoryController#create(CategoryRequest)}
     */
    @Test
    void test_create_when_CategoryNotExists() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(categoryRepository.existsByNameIgnoreCase(any())).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(category);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(123L);
        categoryRequest.setName("Category");
        categoryRequest.setOrdinal(1);
        String content = (new ObjectMapper()).writeValueAsString(categoryRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link CategoryController#findAll(SearchRequest)}
     */
    @Test
    void test_findAll() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(100);

        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        PageImpl<Category> pageImpl = new PageImpl<>(categoryList);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(categoryRepository.findAll((Specification<Category>) any(), (Pageable) any())).thenReturn(pageImpl);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFilters(new ArrayList<>());
        searchRequest.setPage(1);
        searchRequest.setSize(3);
        searchRequest.setSorts(new ArrayList<>());

        String content = (new ObjectMapper()).writeValueAsString(searchRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link CategoryController#create(CategoryRequest)}
     */
    @Test
    void test_create_when_CategoryExists() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(categoryRepository.existsByNameIgnoreCase(any())).thenReturn(true);
        when(categoryRepository.save(any())).thenReturn(category);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(123L);
        categoryRequest.setName("");
        categoryRequest.setOrdinal(1);
        String content = (new ObjectMapper()).writeValueAsString(categoryRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link CategoryController#deleteById(long)}
     */
    @Test
    void test_deleteById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());
        Optional<Category> ofResult = Optional.of(category);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        doNothing().when(categoryRepository).delete(any());
        when(categoryRepository.findById(any())).thenReturn(ofResult);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/categories/{id}", 123L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link CategoryController#update(CategoryRequest)}
     */
    @Test
    void test_update() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Category category = new Category();
        category.setId(123L);
        category.setName("Category");
        category.setOrdinal(1);
        category.setRecipes(new HashSet<>());
        Optional<Category> ofResult = Optional.of(category);

        Category category1 = new Category();
        category1.setId(123L);
        category1.setName("Category");
        category1.setOrdinal(1);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(categoryRepository.existsByNameIgnoreCase(any())).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(category1);
        when(categoryRepository.findById(any())).thenReturn(ofResult);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(123L);
        categoryRequest.setName("Category");
        categoryRequest.setOrdinal(1);

        String content = (new ObjectMapper()).writeValueAsString(categoryRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }
}

