package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.model.Unit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitRequestMapperTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/data/units.csv")
    void mapToEntity_should_return_UnitEntity(Long id, String name) {
        UnitRequest request = new UnitRequest(id, name);
        request.setId(id);
        request.setName(name);

        Unit result = UnitRequestMapper.mapToEntity(request);

        assertEquals(id, result.getId());
        assertEquals(capitalizeFully(name), result.getName());
    }
}
