package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.model.Ingredient;
import org.apache.commons.text.WordUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for IngredientRequest
 */
@Mapper(componentModel = "spring")
public interface IngredientRequestMapper {

    IngredientRequestMapper MAPPER = Mappers.getMapper(IngredientRequestMapper.class);

    Ingredient toEntity(IngredientRequest dto);

    IngredientRequest toDto(Ingredient entity);

    @AfterMapping
    default void capitalizeFully(@MappingTarget Ingredient entity, IngredientRequest dto) {
        entity.setName(WordUtils.capitalizeFully(dto.getName()));
    }
}
