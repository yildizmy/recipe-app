package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.CategoryRequest;
import com.github.yildizmy.model.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryRequestMapperTest {

    @Test
    public void testMapDtoToEntity() {
        CategoryRequest request = new CategoryRequest();
        request.setId(1L);
        request.setName("Category");
        request.setOrdinal(3);

        Category category = CategoryRequestMapper.MAPPER.toEntity(request);

        assertEquals(category.getId(), 1L);
        assertEquals(category.getName(), "Category");
        assertEquals(category.getOrdinal(), 3);
    }
}
