package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.model.Ingredient;

import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Mapper for IngredientRequest
 */
public class IngredientRequestMapper {

    private IngredientRequestMapper() {
    }

    /**
     * Maps IngredientRequest fields to a new Ingredient
     *
     * @param request
     * @return Ingredient model
     */
    public static Ingredient mapToEntity(IngredientRequest request) {
        return new Ingredient(
                request.getId(),
                capitalizeFully(request.getName())
        );
    }
}
