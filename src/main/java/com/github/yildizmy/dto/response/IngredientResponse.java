package com.github.yildizmy.dto.response;

import com.github.yildizmy.model.Ingredient;
import lombok.Data;

/**
 * Data Transfer Object for Ingredient response
 */
@Data
public class IngredientResponse {

    private Long id;
    private String name;

    public IngredientResponse(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
    }
}
