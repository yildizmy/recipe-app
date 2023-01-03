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

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Integer ordinal;
}
