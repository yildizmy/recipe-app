package com.github.yildizmy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId recipeIngredientId = new RecipeIngredientId();

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private Unit unit;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Unit unit, BigDecimal amount) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return recipeIngredientId.equals(that.recipeIngredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeIngredientId);
    }
}
