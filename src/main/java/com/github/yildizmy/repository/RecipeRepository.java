package com.github.yildizmy.repository;

import com.github.yildizmy.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    @Query(value = "SELECT DISTINCT r.id, r.title, r.description, r.prep_time, " +
            "r.cook_time, r.servings, r.instructions, r.difficulty, r.health_label, r.category_id " +
            "FROM Recipe r " +
            "LEFT JOIN category c ON r.category_id = c.id " +
            "LEFT JOIN recipe_ingredient ri ON r.id = ri.recipe_id " +
            "LEFT JOIN ingredient i ON ri.ingredient_id = i.id " +
            "LEFT JOIN unit u ON ri.unit_id = u.id " +
            "WHERE (:isVegetarian IS NULL OR :isVegetarian IS FALSE OR r.health_label = 'VEGETARIAN') " +
            "AND (:isVegetarian IS NULL OR :isVegetarian IS true OR r.health_label <> 'VEGETARIAN') " +
            "AND (:servings IS NULL OR r.servings = :servings) " +
            "AND (:ingredientIn IS NULL OR lower(i.name) = lower(:ingredientIn)) " +
            "AND (:ingredientEx IS NULL OR r.id NOT IN (SELECT DISTINCT ri2.recipe_id FROM recipe_ingredient ri2, ingredient i2 " +
            "WHERE ri2.ingredient_id = i2.id and lower(i2.name) = lower(:ingredientEx))) " +
            "AND (:text IS NULL OR to_tsvector(r.instructions) @@ to_tsquery(:text)) " +
            "ORDER BY r.id", nativeQuery = true)
    List<Recipe> search(@Param("isVegetarian") Boolean isVegetarian,
                        @Param("servings") Integer servings,
                        @Param("ingredientIn") String ingredientIn,
                        @Param("ingredientEx") String ingredientEx,
                        @Param("text") String text);
}
