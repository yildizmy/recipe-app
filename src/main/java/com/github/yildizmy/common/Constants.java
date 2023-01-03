package com.github.yildizmy.common;

import org.springframework.beans.factory.annotation.Value;

import java.time.format.DateTimeFormatter;

/**
 * Constant variables used in the project
 */
public final class Constants {

    private Constants() {}

    @Value("${spring.jackson.date-format}")
    private static String dateFormat;

    public static final String TRACE = "trace";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(dateFormat);
    public static final String PAGE_NO = "0";
    public static final String PAGE_SIZE = "10";
    public static final String SORT_BY_ID = "id";
    public static final String SORT_BY_NAME = "name";
    public static final String SORT_BY_ORDINAL = "ordinal";

    public static final String SUCCESS = "Success";
    public static final String VALIDATION_ERROR = "Validation error. Check 'errors' field for details";
    public static final String FIELD_PARSE_ERROR = "Failed parse field type DATE {}";
    public static final String FIELD_TYPE_ERROR = "Can not use between for {} field type";
    public static final String METHOD_ARGUMENT_NOT_VALID = "MethodArgumentNotValid exception";
    public static final String NOT_FOUND = "Requested element is not found";
    public static final String NOT_FOUND_RECORD = "Not found any record";
    public static final String NOT_FOUND_CATEGORY = "Requested category is not found";
    public static final String NOT_FOUND_INGREDIENT = "Requested ingredient is not found";
    public static final String NOT_FOUND_RECIPE = "Requested recipe is not found";
    public static final String NOT_FOUND_UNIT = "Requested unit is not found";
    public static final String ALREADY_EXISTS = "Requested element already exists";
    public static final String ALREADY_EXISTS_CATEGORY = "Requested category already exists";
    public static final String ALREADY_EXISTS_INGREDIENT = "Requested ingredient already exists";
    public static final String ALREADY_EXISTS_UNIT = "Requested unit already exists";
    public static final String NOT_VALIDATED_ELEMENT = "Failed to validate the input";
    public static final String NOT_VALIDATED_INGREDIENT = "There are duplicate ingredients for the given recipe";
}
