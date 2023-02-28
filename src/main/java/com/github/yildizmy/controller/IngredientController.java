package com.github.yildizmy.controller;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.dto.response.ApiResponse;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.dto.response.IngredientResponse;
import com.github.yildizmy.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.Instant;

import static com.github.yildizmy.common.Constants.SUCCESS;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class IngredientController {

    private final Clock clock;
    private final IngredientService ingredientService;

    /**
     * Fetches ingredient by id
     *
     * @param id
     * @return A single ingredient
     */
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<ApiResponse<IngredientResponse>> findById(@PathVariable long id) {
        final IngredientResponse response = ingredientService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Fetches all ingredients based on the given filter parameters
     *
     * @param request
     * @return Paginated ingredient data
     */
    @GetMapping("/ingredients")
    public ResponseEntity<ApiResponse<Page<IngredientResponse>>> findAll(@RequestBody SearchRequest request) {
        final Page<IngredientResponse> response = ingredientService.findAll(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Creates a new ingredient
     *
     * @return id of the created ingredient
     */
    @PostMapping("/ingredients")
    public ResponseEntity<ApiResponse<CommandResponse>> create(@Valid @RequestBody IngredientRequest request) {
        final CommandResponse response = ingredientService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Updates given ingredient
     *
     * @return id of the updated ingredient
     */
    @PutMapping("/ingredients")
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody IngredientRequest request) {
        final CommandResponse response = ingredientService.update(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Deletes ingredient by id
     *
     * @return id of the deleted ingredient
     */
    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<ApiResponse<CommandResponse>> deleteById(@PathVariable long id) {
        final CommandResponse response = ingredientService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }
}
