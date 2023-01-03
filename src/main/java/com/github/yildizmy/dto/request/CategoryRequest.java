package com.github.yildizmy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object for Category request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    Long id;

    @NotBlank
    String name;

    @NotNull
    Integer ordinal;
}
