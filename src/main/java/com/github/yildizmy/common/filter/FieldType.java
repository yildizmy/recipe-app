package com.github.yildizmy.common.filter;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static com.github.yildizmy.common.Constants.FIELD_PARSE_ERROR;
import static com.github.yildizmy.common.Constants.FORMATTER;

/**
 * Field types used for filtering
 */
@Slf4j
public enum FieldType {

    BOOLEAN {
        public Object parse(String value) {
            return Boolean.valueOf(value);
        }
    },
    CHAR {
        public Object parse(String value) {
            return value.charAt(0);
        }
    },
    DATE {
        public Object parse(String value) {
            Object date = null;
            try {
                date = LocalDateTime.parse(value, FORMATTER);
            } catch (Exception e) {
                log.warn(FIELD_PARSE_ERROR, e.getMessage());
            }
            return date;
        }
    },
    DOUBLE {
        public Object parse(String value) {
            return Double.valueOf(value);
        }
    },
    INTEGER {
        public Object parse(String value) {
            return Integer.valueOf(value);
        }
    },
    LONG {
        public Object parse(String value) {
            return Long.valueOf(value);
        }
    },
    STRING {
        public Object parse(String value) {
            return value;
        }
    };

    public abstract Object parse(String value);
}
