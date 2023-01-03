package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.model.Ingredient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientRequestMapperTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/data/ingredients.csv")
    void mapToEntity_should_return_IngredientEntity(Long id, String name) {
        IngredientRequest request = new IngredientRequest(name);
        request.setId(id);
        request.setName(name);

        Ingredient result = IngredientRequestMapper.mapToEntity(request);

        assertEquals(id, result.getId());
        assertEquals(capitalizeFully(name), result.getName());
    }
}