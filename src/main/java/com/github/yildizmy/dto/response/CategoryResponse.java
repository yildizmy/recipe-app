package com.github.yildizmy.dto.response;

import com.github.yildizmy.model.Category;
import lombok.Data;

/**
 * Data Transfer Object for Category response
 */
@Data
public class CategoryResponse {

    private Long id;
    private String name;
    private int ordinal;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.ordinal = category.getOrdinal();
    }
}
