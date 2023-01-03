package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.model.Unit;

import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Mapper for UnitRequest
 */
public class UnitRequestMapper {

    private UnitRequestMapper() {
    }

    /**
     * Maps UnitRequest fields to a new Unit
     *
     * @param request
     * @return Unit model
     */
    public static Unit mapToEntity(UnitRequest request) {
        return new Unit(
                request.getId(),
                capitalizeFully(request.getName())
        );
    }
}
