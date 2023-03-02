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
    public void testMapDtoToEntity(Long id, String title, String description, Integer prepTime,
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

        Recipe recipe = RecipeRequestMapper.MAPPER.toEntity(request);

        assertEquals(id, recipe.getId());
        assertEquals(capitalizeFully(title), recipe.getTitle());
        assertEquals(description, recipe.getDescription());
        assertEquals(prepTime, recipe.getPrepTime());
        assertEquals(cookTime, recipe.getCookTime());
        assertEquals(servings, recipe.getServings());
        assertEquals(instructions, recipe.getInstructions());
        assertEquals(difficulty, recipe.getDifficulty());
        assertEquals(healthLabel, recipe.getHealthLabel());
    }
}
