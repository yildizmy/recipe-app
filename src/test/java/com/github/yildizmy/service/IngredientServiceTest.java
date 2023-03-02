package com.github.yildizmy.service;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.mapper.IngredientRequestMapper;
import com.github.yildizmy.dto.mapper.IngredientRequestMapperImpl;
import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.dto.response.IngredientResponse;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.model.Ingredient;
import com.github.yildizmy.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Test for IngredientService methods
 */
@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService service;

    @Mock
    private IngredientRepository ingredientRepository;

    @Spy
    public IngredientRequestMapper ingredientRequestMapper = new IngredientRequestMapperImpl();

    @Captor
    private ArgumentCaptor<Ingredient> ingredientCaptor;

    /**
     * Method under test: {@link IngredientService#findById(Long)}
     */
    @Test
    void findById_should_throw_NoSuchElementFoundException_when_IngredientIsNotFound() {
        when(ingredientRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            service.findById(101L);
        });

        verify(ingredientRepository).findById(101L);
    }

    /**
     * Method under test: {@link IngredientService#findById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/ingredients.csv")
    void findById_should_return_IngredientResponse_when_IngredientIsFound(Long id, String name) {
        Ingredient ingredient = new Ingredient(id, name);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        IngredientResponse result = service.findById(id);

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        verify(ingredientRepository).findById(id);
    }

    /**
     * Method under test: {@link IngredientService#findAll(SearchRequest)}
     */
    @Test
    void findAll_should_throw_NoSuchElementFoundException_when_NoIngredientIsFound() {
        SearchRequest request = new SearchRequest();

        when(ingredientRepository.findAll((Specification<Ingredient>) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> service.findAll(request));

        verify(ingredientRepository).findAll((Specification<Ingredient>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link IngredientService#findAll(SearchRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/ingredients.csv")
    void findAll_should_return_IngredientResponseList_when_IngredientIsFound(Long id, String name) {
        Ingredient ingredient = new Ingredient(id, name);
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(ingredient);
        PageImpl<Ingredient> pageImpl = new PageImpl<>(ingredientList);

        when(ingredientRepository.findAll((Specification<Ingredient>) any(), (Pageable) any())).thenReturn(pageImpl);

        List<IngredientResponse> result = service.findAll(new SearchRequest()).toList();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(name, result.get(0).getName());
        verify(ingredientRepository).findAll((Specification<Ingredient>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link IngredientService#create(IngredientRequest)}
     */
    @Test
    void create_should_throw_ElementAlreadyExistsException_when_IngredientExists() {
        IngredientRequest request = new IngredientRequest("Ingredient");

        when(ingredientRepository.existsByNameIgnoreCase(request.getName())).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> service.create(request));

        verify(ingredientRepository, never()).save(any());
    }

    /**
     * Method under test: {@link IngredientService#create(IngredientRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/ingredients.csv")
    void create_should_return_CommandResponse_when_IngredientNotExists(Long id, String name) {
        IngredientRequest request = new IngredientRequest(name);

        when(ingredientRepository.existsByNameIgnoreCase(request.getName())).thenReturn(false);

        service.create(request);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        Ingredient capturedIngredient = ingredientCaptor.getValue();

        assertEquals(capitalizeFully(name), capturedIngredient.getName());
        verify(ingredientRepository).save(capturedIngredient);
    }

    /**
     * Method under test: {@link IngredientService#update(IngredientRequest)}
     */
    @Test
    void update_should_throw_NoSuchElementFoundException_when_IngredientNotFound() {
        IngredientRequest request = new IngredientRequest("Ingredient");

        when(ingredientRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.update(request));

        verify(ingredientRepository, never()).existsByNameIgnoreCase(any());
        verify(ingredientRepository, never()).save(any());
    }

    /**
     * Method under test: {@link IngredientService#update(IngredientRequest)}
     */
    @Test
    void update_should_throw_ElementAlreadyExistsException_when_IngredientExists() {
        IngredientRequest request = new IngredientRequest("Ingredient");

        when(ingredientRepository.findById(request.getId())).thenReturn(Optional.of(new Ingredient()));
        when(ingredientRepository.existsByNameIgnoreCase(request.getName())).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> service.update(request));

        verify(ingredientRepository, never()).save(any());
    }

    /**
     * Method under test: {@link IngredientService#update(IngredientRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/ingredients.csv")
    void update_should_return_CommandResponse_when_IngredientNotExists(Long id, String name) {
        IngredientRequest request = new IngredientRequest(name);
        Ingredient ingredient = new Ingredient(id, name);

        when(ingredientRepository.findById(request.getId())).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.existsByNameIgnoreCase(request.getName())).thenReturn(false);

        service.update(request);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        Ingredient capturedIngredient = ingredientCaptor.getValue();

        assertEquals(id, capturedIngredient.getId());
        assertEquals(capitalizeFully(name), capturedIngredient.getName());
        verify(ingredientRepository).save(capturedIngredient);
    }

    /**
     * Method under test: {@link IngredientService#deleteById(Long)}
     */
    @Test
    void deleteById_should_throw_NoSuchElementFoundException_when_IngredientNotExists() {
        when(ingredientRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.deleteById(101L));

        verify(ingredientRepository, never()).delete(any());
    }

    /**
     * Method under test: {@link IngredientService#deleteById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/ingredients.csv")
    void deleteById_should_return_CommandResponse_when_IngredientIsFound(Long id, String name) {
        Ingredient ingredient = new Ingredient(id, name);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        service.deleteById(id);
        verify(ingredientRepository).delete(ingredientCaptor.capture());
        Ingredient capturedIngredient = ingredientCaptor.getValue();

        assertEquals(id, capturedIngredient.getId());
        assertEquals(name, capturedIngredient.getName());
        verify(ingredientRepository).delete(capturedIngredient);
    }
}


