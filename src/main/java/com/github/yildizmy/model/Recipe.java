package com.github.yildizmy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 100)
    private String description;

    private Integer prepTime;

    private Integer cookTime;

    private Integer servings;

    @Lob
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @Column(nullable = false)
    private String instructions;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(value = EnumType.STRING)
    private HealthLabel healthLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredients.add(recipeIngredient);
        recipeIngredient.setRecipe(this);
    }

    public void removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredients.remove(recipeIngredient);
        recipeIngredient.setRecipe(null);
    }

    public Recipe(Long id) {
        this.id = id;
    }

    public Recipe(Long id, String title, String description, Integer prepTime, Integer cookTime,
                  Integer servings, String instructions, Difficulty difficulty, HealthLabel healthLabel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.instructions = instructions;
        this.difficulty = difficulty;
        this.healthLabel = healthLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return title.equals(recipe.title) &&
                Objects.equals(description, recipe.description) &&
                Objects.equals(prepTime, recipe.prepTime) &&
                Objects.equals(cookTime, recipe.cookTime) &&
                Objects.equals(servings, recipe.servings) &&
                instructions.equals(recipe.instructions) &&
                difficulty == recipe.difficulty &&
                healthLabel == recipe.healthLabel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, prepTime, cookTime, servings, instructions, difficulty, healthLabel);
    }
}
