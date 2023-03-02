package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.RecipeRequest;
import com.github.yildizmy.model.Recipe;
import org.apache.commons.text.WordUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for RecipeRequest
 */
@Mapper(componentModel = "spring")
public interface RecipeRequestMapper {

    RecipeRequestMapper MAPPER = Mappers.getMapper(RecipeRequestMapper.class);

    Recipe toEntity(RecipeRequest dto);

    RecipeRequest toDto(Recipe entity);

    @AfterMapping
    default void getCapitalizedTitle(@MappingTarget Recipe entity, RecipeRequest dto) {
        entity.setTitle(WordUtils.capitalizeFully(dto.getTitle()));
    }
}
