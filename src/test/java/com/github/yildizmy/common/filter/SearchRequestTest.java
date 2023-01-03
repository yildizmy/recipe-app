package com.github.yildizmy.common.filter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchRequestTest {

    /**
     * Method under test: {@link SearchRequest#getFilters()}
     */
    @Test
    void getFilters_should_returnNewArrayList_when_filtersNull() {
        assertTrue((new SearchRequest()).getFilters().isEmpty());
    }

    /**
     * Method under test: {@link SearchRequest#getFilters()}
     */
    @Test
    void getFilters_should_returnFilters_when_filtersNotNull() {
        ArrayList<FilterRequest> filterRequestList = new ArrayList<>();
        List<FilterRequest> actualFilters = (new SearchRequest(filterRequestList, new ArrayList<>(), 1, 3)).getFilters();
        assertSame(filterRequestList, actualFilters);
        assertTrue(actualFilters.isEmpty());
    }

    /**
     * Method under test: {@link SearchRequest#getSorts()}
     */
    @Test
    void getSorts_should_returnNewArrayList_when_sortsNull() {
        assertTrue((new SearchRequest()).getSorts().isEmpty());
    }

    /**
     * Method under test: {@link SearchRequest#getSorts()}
     */
    @Test
    void getFilters_should_returnSorts_when_sortsNotNull() {
        ArrayList<FilterRequest> filters = new ArrayList<>();
        ArrayList<SortRequest> sortRequestList = new ArrayList<>();
        List<SortRequest> actualSorts = (new SearchRequest(filters, sortRequestList, 1, 3)).getSorts();
        assertSame(sortRequestList, actualSorts);
        assertTrue(actualSorts.isEmpty());
    }
}
