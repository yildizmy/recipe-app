package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.RecipeRequest;
import com.github.yildizmy.model.Recipe;

import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Mapper for RecipeRequest
 */
public class RecipeRequestMapper {

    private RecipeRequestMapper() {
    }

    /**
     * Maps RecipeRequest fields to a new Recipe
     *
     * @param request
     * @return Recipe model
     */
    public static Recipe mapToEntity(RecipeRequest request) {
        return new Recipe(
                request.getId(),
                capitalizeFully(request.getTitle()),
                request.getDescription(),
                request.getPrepTime(),
                request.getCookTime(),
                request.getServings(),
                request.getInstructions(),
                request.getDifficulty(),
                request.getHealthLabel()
        );
    }
}
