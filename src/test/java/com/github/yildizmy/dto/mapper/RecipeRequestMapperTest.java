package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.RecipeRequest;
import com.github.yildizmy.model.Difficulty;
import com.github.yildizmy.model.HealthLabel;
import com.github.yildizmy.model.Recipe;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeRequestMapperTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/data/recipes.csv")
    void mapToEntity_should_return_RecipeEntity(Long id, String title, String description, Integer prepTime,
                                                Integer cookTime, Integer servings, String instructions,
                                                Difficulty difficulty, HealthLabel healthLabel) {
        RecipeRequest request = new RecipeRequest();
        request.setId(id);
        request.setTitle(title);
        request.setDescription(description);
        request.setPrepTime(prepTime);
        request.setCookTime(cookTime);
        request.setServings(servings);
        request.setInstructions(instructions);
        request.setDifficulty(difficulty);
        request.setHealthLabel(healthLabel);

        Recipe result = RecipeRequestMapper.mapToEntity(request);

        assertEquals(id, result.getId());
        assertEquals(capitalizeFully(title), result.getTitle());
        assertEquals(description, result.getDescription());
        assertEquals(prepTime, result.getPrepTime());
        assertEquals(cookTime, result.getCookTime());
        assertEquals(servings, result.getServings());
        assertEquals(instructions, result.getInstructions());
        assertEquals(difficulty, result.getDifficulty());
        assertEquals(healthLabel, result.getHealthLabel());
    }
}
