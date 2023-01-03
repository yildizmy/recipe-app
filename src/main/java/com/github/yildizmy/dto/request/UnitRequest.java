package com.github.yildizmy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object for Unit request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitRequest {

    private Long id;

    @NotBlank
    private String name;
}
