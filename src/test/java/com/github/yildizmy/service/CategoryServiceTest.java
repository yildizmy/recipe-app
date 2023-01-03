package com.github.yildizmy.service;

import com.github.yildizmy.dto.response.CategoryResponse;
import com.github.yildizmy.model.Category;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.dto.request.CategoryRequest;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.text.WordUtils.capitalizeFully;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Test for CategoryService methods
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    /**
     * Method under test: {@link CategoryService#findById(Long)}
     */
    @Test
    void findById_should_throw_NoSuchElementFoundException_when_CategoryIsNotFound() {
        when(categoryRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            service.findById(101L);
        });

        verify(categoryRepository).findById(101L);
    }

    /**
     * Method under test: {@link CategoryService#findById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/categories.csv")
    void findById_should_return_CategoryResponse_when_CategoryIsFound(Long id, String name, Integer ordinal) {
        Category category = new Category(id, name, ordinal);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryResponse result = service.findById(id);

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(ordinal, result.getOrdinal());
        verify(categoryRepository).findById(id);
    }

    /**
     * Method under test: {@link CategoryService#findAll(SearchRequest)}
     */
    @Test
    void findAll_should_throw_NoSuchElementFoundException_when_NoCategoryIsFound() {
        SearchRequest request = new SearchRequest();

        when(categoryRepository.findAll((Specification<Category>) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> service.findAll(request));

        verify(categoryRepository).findAll((Specification<Category>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link CategoryService#findAll(SearchRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/categories.csv")
    void findAll_should_return_CategoryResponseList_when_CategoryIsFound(Long id, String name, Integer ordinal) {
        Category category = new Category(id, name, ordinal);
        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        PageImpl<Category> pageImpl = new PageImpl<>(categoryList);

        when(categoryRepository.findAll((Specification<Category>) any(), (Pageable) any())).thenReturn(pageImpl);

        List<CategoryResponse> result = service.findAll(new SearchRequest()).toList();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(name, result.get(0).getName());
        assertEquals(ordinal, result.get(0).getOrdinal());
        verify(categoryRepository).findAll((Specification<Category>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link CategoryService#create(CategoryRequest)}
     */
    @Test
    void create_should_throw_ElementAlreadyExistsException_when_CategoryExists() {
        CategoryRequest request = new CategoryRequest(101L, "Category", 1);

        when(categoryRepository.existsByNameIgnoreCase(request.getName())).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> service.create(request));

        verify(categoryRepository, never()).save(any());
    }

    /**
     * Method under test: {@link CategoryService#create(CategoryRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/categories.csv")
    void create_should_return_CommandResponse_when_CategoryNotExists(Long id, String name, Integer ordinal) {
        CategoryRequest request = new CategoryRequest(id, name, ordinal);

        when(categoryRepository.existsByNameIgnoreCase(request.getName())).thenReturn(false);

        service.create(request);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        assertEquals(id, capturedCategory.getId());
        assertEquals(capitalizeFully(name), capturedCategory.getName());
        assertEquals(ordinal, capturedCategory.getOrdinal());
        verify(categoryRepository).save(capturedCategory);
    }

    /**
     * Method under test: {@link CategoryService#update(CategoryRequest)}
     */
    @Test
    void update_should_throw_NoSuchElementFoundException_when_CategoryNotExists() {
        CategoryRequest request = new CategoryRequest(101L, "Category", 1);

        when(categoryRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.update(request));

        verify(categoryRepository, never()).existsByNameIgnoreCase(any());
        verify(categoryRepository, never()).save(any());
    }

    /**
     * Method under test: {@link CategoryService#update(CategoryRequest)}
     */
    @Test
    void update_should_throw_ElementAlreadyExistsException_when_CategoryExists() {
        CategoryRequest request = new CategoryRequest(101L, "Category", 1);

        when(categoryRepository.findById(request.getId())).thenReturn(Optional.of(new Category()));
        when(categoryRepository.existsByNameIgnoreCase(request.getName())).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> service.update(request));

        verify(categoryRepository, never()).save(any());
    }

    /**
     * Method under test: {@link CategoryService#update(CategoryRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/categories.csv")
    void update_should_return_CommandResponse_when_CategoryNotExists(Long id, String name, Integer ordinal) {
        CategoryRequest request = new CategoryRequest(id, name, ordinal);
        Category category = new Category(id, name, ordinal);

        when(categoryRepository.findById(request.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase(request.getName())).thenReturn(false);

        service.update(request);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        assertEquals(id, capturedCategory.getId());
        assertEquals(capitalizeFully(name), capturedCategory.getName());
        assertEquals(ordinal, capturedCategory.getOrdinal());
        verify(categoryRepository).save(capturedCategory);
    }

    /**
     * Method under test: {@link CategoryService#deleteById(Long)}
     */
    @Test
    void deleteById_should_throw_NoSuchElementFoundException_when_CategoryNotExists() {
        when(categoryRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.deleteById(101L));

        verify(categoryRepository, never()).delete(any());
    }

    /**
     * Method under test: {@link CategoryService#deleteById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/categories.csv")
    void deleteById_should_return_CommandResponse_when_CategoryIsFound(Long id, String name, Integer ordinal) {
        Category category = new Category(id, name, ordinal);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        service.deleteById(id);
        verify(categoryRepository).delete(categoryCaptor.capture());
        Category capturedCategory = categoryCaptor.getValue();

        assertEquals(id, capturedCategory.getId());
        assertEquals(name, capturedCategory.getName());
        assertEquals(ordinal, capturedCategory.getOrdinal());
        verify(categoryRepository).delete(capturedCategory);
    }
}
