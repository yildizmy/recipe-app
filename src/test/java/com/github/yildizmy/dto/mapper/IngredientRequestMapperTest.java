package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.model.Ingredient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientRequestMapperTest {

    @Test
    public void testMapDtoToEntity() {
        IngredientRequest request = new IngredientRequest();
        request.setId(1L);
        request.setName("Ingredient");

        Ingredient ingredient = IngredientRequestMapper.MAPPER.toEntity(request);

        assertEquals(ingredient.getId(), 1L);
        assertEquals(ingredient.getName(), "Ingredient");
    }
}