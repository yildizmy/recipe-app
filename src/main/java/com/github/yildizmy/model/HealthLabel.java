package com.github.yildizmy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HealthLabel {

    DEFAULT("Default"),
    EGG_FREE("Egg-free"),
    GLUTEN_FREE("Gluten-free"),
    DIABETIC("Diabetic"),
    NO_SUGAR("No-sugar"),
    RED_MEAT_FREE("Red-meat-free"),
    VEGETARIAN("Vegetarian"),
    WHEAT_FREE("Wheat-free");

    private String label;
}
