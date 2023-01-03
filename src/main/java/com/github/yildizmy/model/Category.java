package com.github.yildizmy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer ordinal;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Recipe> recipes = new HashSet<>();

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        recipe.setCategory(this);
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        recipe.setCategory(null);
    }

    public Category(Long id, String name, int ordinal) {
        this.id = id;
        this.name = name;
        this.ordinal = ordinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
