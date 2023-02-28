package com.github.yildizmy.controller;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.CategoryRequest;
import com.github.yildizmy.dto.response.ApiResponse;
import com.github.yildizmy.dto.response.CategoryResponse;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.service.CategoryService;
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
public class CategoryController {

    private final Clock clock;
    private final CategoryService categoryService;

    /**
     * Fetches category by id
     *
     * @param id
     * @return A single category
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> findById(@PathVariable long id) {
        final CategoryResponse response = categoryService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Fetches all categories based on the given filter parameters
     *
     * @param request
     * @return Paginated category data
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> findAll(@RequestBody SearchRequest request) {
        final Page<CategoryResponse> response = categoryService.findAll(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Creates a new category
     *
     * @return id of the created category
     */
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CommandResponse>> create(@Valid @RequestBody CategoryRequest request) {
        final CommandResponse response = categoryService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Updates given category
     *
     * @return id of the updated category
     */
    @PutMapping("/categories")
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody CategoryRequest request) {
        final CommandResponse response = categoryService.update(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Deletes category by id
     *
     * @return id of the deleted category
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CommandResponse>> deleteById(@PathVariable long id) {
        final CommandResponse response = categoryService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }
}
