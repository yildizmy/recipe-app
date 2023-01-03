package com.github.yildizmy.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Data Transfer Object for Recipe search
 */
@Data
@NoArgsConstructor
public class RecipeSearchRequest {

    Boolean isVegetarian;
    Integer servings;
    String ingredientIn;
    String ingredientEx;
    String text;

    public Boolean getIsVegetarian() {
        return isVegetarian;
    }

    public Integer getServings() {
        return servings;
    }

    public String getIngredientIn() {
        return StringUtils.isBlank(ingredientIn) ? null : ingredientIn;
    }

    public String getIngredientEx() {
        return StringUtils.isBlank(ingredientEx) ? null : ingredientEx;
    }

    public String getText() {
        return StringUtils.isBlank(text) ? null : text;
    }
}
