package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.model.Category;
import com.github.yildizmy.dto.request.CategoryRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryRequestMapperTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/data/categories.csv")
    void mapToEntity_should_return_CategoryEntity(Long id, String name, Integer ordinal) {
        CategoryRequest request = new CategoryRequest(id, name, ordinal);

        Category result = CategoryRequestMapper.mapToEntity(request);

        assertEquals(id, result.getId());
        assertEquals(capitalizeFully(name), result.getName());
        assertEquals(ordinal, result.getOrdinal());
    }
}
