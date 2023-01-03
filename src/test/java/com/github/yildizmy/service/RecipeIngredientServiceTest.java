package com.github.yildizmy.service;

import com.github.yildizmy.model.RecipeIngredient;
import com.github.yildizmy.model.Unit;
import com.github.yildizmy.dto.request.RecipeIngredientRequest;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.model.Ingredient;
import com.github.yildizmy.model.Recipe;
import com.github.yildizmy.repository.IngredientRepository;
import com.github.yildizmy.repository.RecipeIngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Recipe Test for RecipeIngredientService methods
 */
@ExtendWith(MockitoExtension.class)
class RecipeIngredientServiceTest {

    @InjectMocks
    private RecipeIngredientService service;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Captor
    private ArgumentCaptor<RecipeIngredient> recipeIngredientCaptor;

    /**
     * Method under test: {@link RecipeIngredientService#addIngredientToRecipe(RecipeIngredientRequest)}
     */
    @Test
    void addIngredientToRecipe_should_throw_ElementAlreadyExistsException_when_when_RecipeIngredientExists() {
        RecipeIngredientRequest request = new RecipeIngredientRequest();
        request.setRecipeId(101L);
        request.setIngredientId(201L);

        when(recipeIngredientRepository.existsByRecipeIdAndIngredientId(101L, 201L)).thenReturn(true);


        assertThrows(ElementAlreadyExistsException.class, () -> {
            service.addIngredientToRecipe(request);
        });

        verify(ingredientRepository, never()).existsByNameIgnoreCase(any());
        verify(ingredientRepository, never()).save(any());
        verify(recipeIngredientRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RecipeIngredientService#addIngredientToRecipe(RecipeIngredientRequest)}
     */
    @Test
    void addIngredientToRecipe_should_throw_ElementAlreadyExistsException_when_when_IngredientWithSameNameExists() {
        Ingredient ingredient = new Ingredient(0L, "Ingredient");

        RecipeIngredientRequest request = new RecipeIngredientRequest();
        request.setRecipeId(101L);
        request.setIngredientId(ingredient.getId());
        request.setIngredientName(ingredient.getName());

        when(ingredientRepository.existsByNameIgnoreCase(ingredient.getName())).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> {
            service.addIngredientToRecipe(request);
        });

        verify(recipeIngredientRepository, never()).existsByRecipeIdAndIngredientId(any(), any());
        verify(recipeIngredientRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RecipeIngredientService#addIngredientToRecipe(RecipeIngredientRequest)}
     */
    @Test
    void addIngredientToRecipe_should_saveNewIngredient_when_when_IngredientWithSameNameNotExists() {
        Ingredient ingredient = new Ingredient(0L, "Ingredient");

        RecipeIngredientRequest request = new RecipeIngredientRequest();
        request.setRecipeId(101L);
        request.setIngredientId(ingredient.getId());
        request.setIngredientName(ingredient.getName());

        when(ingredientRepository.existsByNameIgnoreCase(ingredient.getName())).thenReturn(false);

        service.addIngredientToRecipe(request);

        verify(ingredientRepository).save(ingredient);
        verify(recipeIngredientRepository).save(any());
    }

    /**
     * Method under test: {@link RecipeIngredientService#addIngredientToRecipe(RecipeIngredientRequest)}
     */
    @Test
    void addIngredientToRecipe_should_return_CommandResponse_when_when_RecipeIngredientNotExists() {
        RecipeIngredientRequest request = new RecipeIngredientRequest();
        request.setRecipeId(101L);
        request.setIngredientId(201L);
        request.setAmount(BigDecimal.valueOf(250));
        request.setUnitId(1L);

        when(recipeIngredientRepository.existsByRecipeIdAndIngredientId(101L, 201L)).thenReturn(false);

        service.addIngredientToRecipe(request);
        verify(recipeIngredientRepository).save(recipeIngredientCaptor.capture());
        RecipeIngredient capturedRecipeIngredient = recipeIngredientCaptor.getValue();

        assertEquals(101L, capturedRecipeIngredient.getRecipe().getId());
        assertEquals(201L, capturedRecipeIngredient.getIngredient().getId());
        assertEquals(BigDecimal.valueOf(250), capturedRecipeIngredient.getAmount());
        assertEquals(1L, capturedRecipeIngredient.getUnit().getId());

        verify(ingredientRepository, never()).existsByNameIgnoreCase(any());
        verify(ingredientRepository, never()).save(any());
    }

    /**
     * Method under test: {@link RecipeIngredientService#removeIngredientFromRecipe(Long recipeId, Long ingredientId)}
     */
    @Test
    void removeIngredientFromRecipe_should_throw_NoSuchElementFoundException_when_RecipeIngredientNotFound() {
        when(recipeIngredientRepository.findByRecipeIdAndIngredientId(101L, 201L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.removeIngredientFromRecipe(101L, 201L));

        verify(recipeIngredientRepository, never()).delete(any());
    }

    /**
     * Method under test: {@link RecipeIngredientService#removeIngredientFromRecipe(Long recipeId, Long ingredientId)}
     */
    @Test
    void removeIngredientFromRecipe_should_return_CommandResponse_when_RecipeIngredientFound() {
        Recipe recipe = new Recipe(101L);
        Ingredient ingredient = new Ingredient(201L, "Ingredient");
        Unit unit = new Unit(1L, "Gram");

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setAmount(BigDecimal.valueOf(250));
        recipeIngredient.setUnit(unit);

        when(recipeIngredientRepository.findByRecipeIdAndIngredientId(recipe.getId(), ingredient.getId()))
                .thenReturn(Optional.of(recipeIngredient));

        service.removeIngredientFromRecipe(recipe.getId(), ingredient.getId());
        verify(recipeIngredientRepository).delete(recipeIngredientCaptor.capture());
        RecipeIngredient capturedRecipeIngredient = recipeIngredientCaptor.getValue();

        assertEquals(recipe.getId(), capturedRecipeIngredient.getRecipe().getId());
        assertEquals(ingredient.getId(), capturedRecipeIngredient.getIngredient().getId());
        assertEquals(ingredient.getName(), capturedRecipeIngredient.getIngredient().getName());
        assertEquals(unit.getId(), capturedRecipeIngredient.getUnit().getId());
        assertEquals(unit.getName(), capturedRecipeIngredient.getUnit().getName());
        assertEquals(BigDecimal.valueOf(250), capturedRecipeIngredient.getAmount());
    }
}
