package com.github.yildizmy.dto.response;

import com.github.yildizmy.model.Unit;
import lombok.Data;

/**
 * Data Transfer Object for Unit response
 */
@Data
public class UnitResponse {

    private Long id;
    private String name;

    public UnitResponse(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
    }
}
