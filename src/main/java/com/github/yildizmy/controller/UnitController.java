package com.github.yildizmy.controller;

import com.github.yildizmy.common.Constants;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.dto.response.ApiResponse;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.dto.response.UnitResponse;
import com.github.yildizmy.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UnitController {

    private final Clock clock;
    private final UnitService unitService;

    /**
     * Fetches unit by id
     *
     * @param id
     * @return A single unit
     */
    @GetMapping("/units/{id}")
    public ResponseEntity<ApiResponse<UnitResponse>> findById(@PathVariable long id) {
        final UnitResponse response = unitService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), Constants.SUCCESS, response));
    }

    /**
     * Fetches all units based on the given filter parameters
     *
     * @param request
     * @return Paginated unit data
     */
    @GetMapping("/units")
    public ResponseEntity<ApiResponse<Page<UnitResponse>>> findAll(@RequestBody SearchRequest request) {
        final Page<UnitResponse> response = unitService.findAll(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), Constants.SUCCESS, response));
    }

    /**
     * Creates a new unit
     *
     * @return id of the created unit
     */
    @PostMapping("/units")
    public ResponseEntity<ApiResponse<CommandResponse>> create(@Valid @RequestBody UnitRequest request) {
        final CommandResponse response = unitService.create(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), Constants.SUCCESS, response));
    }

    /**
     * Updates given unit
     *
     * @return id of the updated unit
     */
    @PutMapping("/units")
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody UnitRequest request) {
        final CommandResponse response = unitService.update(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), Constants.SUCCESS, response));
    }

    /**
     * Deletes unit by id
     *
     * @return id of the deleted unit
     */
    @DeleteMapping("/units/{id}")
    public ResponseEntity<ApiResponse<CommandResponse>> deleteById(@PathVariable long id) {
        final CommandResponse response = unitService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), Constants.SUCCESS, response));
    }
}
