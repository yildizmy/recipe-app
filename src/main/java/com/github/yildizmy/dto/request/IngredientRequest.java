package com.github.yildizmy.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object for Ingredient request
 */
@Data
@NoArgsConstructor
public class IngredientRequest {

    private Long id;

    @NotBlank
    private String name;

    public IngredientRequest(String name) {
        this.id = 0L;
        this.name = name;
    }
}
