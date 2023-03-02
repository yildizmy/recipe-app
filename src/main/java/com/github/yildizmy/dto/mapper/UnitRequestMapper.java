package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.model.Unit;
import org.apache.commons.text.WordUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for UnitRequest
 */
@Mapper(componentModel = "spring")
public interface UnitRequestMapper {

    UnitRequestMapper MAPPER = Mappers.getMapper(UnitRequestMapper.class);

    Unit toEntity(UnitRequest dto);

    UnitRequest toDto(Unit entity);

    @AfterMapping
    default void capitalizeFully(@MappingTarget Unit entity, UnitRequest dto) {
        entity.setName(WordUtils.capitalizeFully(dto.getName()));
    }
}
