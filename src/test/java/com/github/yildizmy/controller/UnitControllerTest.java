package com.github.yildizmy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.model.Unit;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.repository.UnitRepository;
import com.github.yildizmy.service.UnitService;
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

@ContextConfiguration(classes = {UnitController.class, UnitService.class})
@ExtendWith(SpringExtension.class)
class UnitControllerTest {

    @MockBean
    private Clock clock;

    @Autowired
    private UnitController unitController;

    @MockBean
    private UnitRepository unitRepository;

    /**
     * Method under test: {@link UnitController#findById(long)}
     */
    @Test
    void test_findById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());
        Optional<Unit> ofResult = Optional.of(unit);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(unitRepository.findById(any())).thenReturn(ofResult);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/units/{id}", 123L);
        MockMvcBuilders.standaloneSetup(unitController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123,\"name\":\"Unit\"}}"));
    }

    /**
     * Method under test: {@link UnitController#create(UnitRequest)}
     */
    @Test
    void test_create() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(unitRepository.existsByNameIgnoreCase((String) any())).thenReturn(false);
        when(unitRepository.save(any())).thenReturn(unit);

        UnitRequest unitRequest = new UnitRequest();
        unitRequest.setId(123L);
        unitRequest.setName("Unit");

        String content = (new ObjectMapper()).writeValueAsString(unitRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(unitController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link UnitController#findAll(SearchRequest)}
     */
    @Test
    void test_findAll() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());

        ArrayList<Unit> unitList = new ArrayList<>();
        unitList.add(unit);
        PageImpl<Unit> pageImpl = new PageImpl<>(unitList);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(unitRepository.findAll((Specification<Unit>) any(), (Pageable) any())).thenReturn(pageImpl);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFilters(new ArrayList<>());
        searchRequest.setPage(1);
        searchRequest.setSize(3);
        searchRequest.setSorts(new ArrayList<>());

        String content = (new ObjectMapper()).writeValueAsString(searchRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(unitController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("{\"timestamp\":1640995200000,\"message\":\"Success\"," +
                        "\"data\":{\"content\":[{\"id\":123,\"name\":\"Unit\"}],\"pageable\":\"INSTANCE\",\"last\":true," +
                        "\"totalElements\":1,\"totalPages\":1,\"size\":1,\"number\":0,\"sort\":{\"empty\":true,\"unsorted\":true," +
                        "\"sorted\":false},\"first\":true,\"numberOfElements\":1,\"empty\":false}}", false));
    }

    /**
     * Method under test: {@link UnitController#create(UnitRequest)}
     */
    @Test
    void test_create2() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(unitRepository.existsByNameIgnoreCase((String) any())).thenReturn(true);
        when(unitRepository.save(any())).thenReturn(unit);

        UnitRequest unitRequest = new UnitRequest();
        unitRequest.setId(123L);
        unitRequest.setName("");

        String content = (new ObjectMapper()).writeValueAsString(unitRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(unitController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link UnitController#deleteById(long)}
     */
    @Test
    void test_deleteById() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());
        Optional<Unit> ofResult = Optional.of(unit);

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        doNothing().when(unitRepository).delete(any());
        when(unitRepository.findById(any())).thenReturn(ofResult);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/units/{id}", 123L);
        MockMvcBuilders.standaloneSetup(unitController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }

    /**
     * Method under test: {@link UnitController#update(UnitRequest)}
     */
    @Test
    void test_update() throws Exception {
        LocalDateTime dateTime = LocalDate.of(2022, 1, 1).atStartOfDay();

        Unit unit = new Unit();
        unit.setId(123L);
        unit.setName("Unit");
        unit.setRecipeIngredients(new HashSet<>());
        Optional<Unit> ofResult = Optional.of(unit);

        Unit unit1 = new Unit();
        unit1.setId(123L);
        unit1.setName("Unit");
        unit1.setRecipeIngredients(new HashSet<>());

        when(clock.instant()).thenReturn(dateTime.atZone(ZoneId.of("UTC")).toInstant());
        when(unitRepository.existsByNameIgnoreCase((String) any())).thenReturn(false);
        when(unitRepository.save(any())).thenReturn(unit1);
        when(unitRepository.findById(any())).thenReturn(ofResult);

        UnitRequest unitRequest = new UnitRequest();
        unitRequest.setId(123L);
        unitRequest.setName("Unit");

        String content = (new ObjectMapper()).writeValueAsString(unitRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(unitController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"timestamp\":1640995200000,\"message\":\"Success\",\"data\":{\"id\":123}}"));
    }
}

