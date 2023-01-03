package com.github.yildizmy.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * Helper method for utility methods
 */
@Component
public class StringUtils {

    private StringUtils() {
    }

    /**
     * Checks if all the given list values are unique or not
     *
     * @param list
     * @param <T>
     * @return
     */
    public <T> boolean areAllUnique(List<T> list) {

        return list.stream().allMatch(new HashSet<>()::add);
    }
}
