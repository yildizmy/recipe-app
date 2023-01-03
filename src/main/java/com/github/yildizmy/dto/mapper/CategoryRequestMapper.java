package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.CategoryRequest;
import com.github.yildizmy.model.Category;

import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Mapper for CategoryRequest
 */
public class CategoryRequestMapper {

    private CategoryRequestMapper() {
    }

    /**
     * Maps CategoryRequest fields to a new Category
     *
     * @param request
     * @return Category model
     */
    public static Category mapToEntity(CategoryRequest request) {
        return new Category(
                request.getId(),
                capitalizeFully(request.getName()),
                request.getOrdinal()
        );
    }
}
