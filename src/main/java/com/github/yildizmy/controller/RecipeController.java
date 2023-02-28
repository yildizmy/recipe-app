package com.github.yildizmy.controller;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.RecipeRequest;
import com.github.yildizmy.dto.request.RecipeSearchRequest;
import com.github.yildizmy.dto.response.ApiResponse;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.dto.response.RecipeResponse;
import com.github.yildizmy.service.RecipeService;
import com.github.yildizmy.validator.ValidIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static com.github.yildizmy.common.Constants.NOT_VALIDATED_INGREDIENT;
import static com.github.yildizmy.common.Constants.SUCCESS;

@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RecipeController {

    private final Clock clock;
    private final RecipeService recipeService;

    /**
     * Fetches recipe by id
     *
     * @param id
     * @return A single recipe
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<ApiResponse<RecipeResponse>> findById(@PathVariable long id) {
        final RecipeResponse response = recipeService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Fetches all recipes based on the given recipe filter parameters
     *
     * @param request
     * @return Paginated recipe data
     */
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<Page<RecipeResponse>>> findAll(@RequestBody SearchRequest request) {
        final Page<RecipeResponse> response = recipeService.findAll(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Search recipes based on the given recipe and ingredient parameters
     *
     * @param request
     * @return Recipe list
     */
    @GetMapping("/recipes/search")
    public ResponseEntity<ApiResponse<List<RecipeResponse>>> search(@RequestBody RecipeSearchRequest request) {
        final List<RecipeResponse> response = recipeService.search(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Creates a new recipe
     *
     * @return id of the created recipe
     */
    @PostMapping("/recipes")
    public ResponseEntity<ApiResponse<CommandResponse>> create(
            @Valid @ValidIngredient(message = NOT_VALIDATED_INGREDIENT) @RequestBody RecipeRequest request) {
        final CommandResponse response = recipeService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Updates given recipe
     *
     * @return id of the updated recipe
     */
    @PutMapping("/recipes")
    public ResponseEntity<ApiResponse<CommandResponse>> update(
            @Valid @ValidIngredient(message = NOT_VALIDATED_INGREDIENT) @RequestBody RecipeRequest request) {
        final CommandResponse response = recipeService.update(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Deletes recipe by id
     *
     * @return id of the deleted recipe
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable long id) {
        recipeService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
