package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.model.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitRequestMapperTest {

    @Test
    public void testMapDtoToEntity() {
        UnitRequest request = new UnitRequest();
        request.setId(1L);
        request.setName("Unit");

        Unit unit = UnitRequestMapper.MAPPER.toEntity(request);

        assertEquals(unit.getId(), 1L);
        assertEquals(unit.getName(), "Unit");
    }
}
