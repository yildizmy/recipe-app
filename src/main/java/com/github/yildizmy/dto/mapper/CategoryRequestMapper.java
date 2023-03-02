package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.CategoryRequest;
import com.github.yildizmy.model.Category;
import org.apache.commons.text.WordUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for CategoryRequest
 */
@Mapper(componentModel = "spring")
public interface CategoryRequestMapper {

    CategoryRequestMapper MAPPER = Mappers.getMapper(CategoryRequestMapper.class);

    Category toEntity(CategoryRequest dto);

    CategoryRequest toDto(Category entity);

    @AfterMapping
    default void capitalizeFully(@MappingTarget Category entity, CategoryRequest dto) {
        entity.setName(WordUtils.capitalizeFully(dto.getName()));
    }
}
