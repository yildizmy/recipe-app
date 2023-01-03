package com.github.yildizmy.repository;

import com.github.yildizmy.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    Optional<RecipeIngredient> findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    boolean existsByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
