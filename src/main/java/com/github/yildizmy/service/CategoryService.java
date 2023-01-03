package com.github.yildizmy.service;

import com.github.yildizmy.common.Constants;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.common.filter.SearchSpecification;
import com.github.yildizmy.dto.response.CategoryResponse;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.yildizmy.dto.mapper.CategoryRequestMapper;
import com.github.yildizmy.dto.request.CategoryRequest;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Service used for adding, updating, removing and fetching categories
 */
@Slf4j(topic = "CategoryService")
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Fetches a category by the given id
     *
     * @param id
     * @return
     */
    public CategoryResponse findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponse::new)
                .orElseThrow(() -> {
                    log.error(Constants.NOT_FOUND_CATEGORY);
                    return new NoSuchElementFoundException(Constants.NOT_FOUND_CATEGORY);
                });
    }

    /**
     * Fetches all categories based on the given filter parameters
     *
     * @param request
     * @return Paginated category data
     */
    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAll(SearchRequest request) {
        final SearchSpecification<Category> specification = new SearchSpecification<>(request);
        final Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        final Page<CategoryResponse> categories = categoryRepository.findAll(specification, pageable)
                .map(CategoryResponse::new);
        if (categories.isEmpty()) {
            log.error(Constants.NOT_FOUND_RECORD);
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        }
        return categories;
    }

    /**
     * Creates a new category using the given request parameters
     *
     * @param request
     * @return
     */
    public CommandResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            log.error(Constants.ALREADY_EXISTS_CATEGORY);
            throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_CATEGORY);
        }
        final Category category = CategoryRequestMapper.mapToEntity(request);
        categoryRepository.save(category);
        return CommandResponse.builder().id(category.getId()).build();
    }

    /**
     * Updates category using the given request parameters
     *
     * @param request
     * @return
     */
    public CommandResponse update(CategoryRequest request) {
        final Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.error(Constants.NOT_FOUND_CATEGORY);
                    return new NoSuchElementFoundException(Constants.NOT_FOUND_CATEGORY);
                });

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            log.error(Constants.ALREADY_EXISTS_CATEGORY);
            throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_CATEGORY);
        }
        category.setName(capitalizeFully(request.getName()));
        categoryRepository.save(category);
        return CommandResponse.builder().id(category.getId()).build();
    }

    /**
     * Deletes category by the given id
     *
     * @param id
     * @return
     */
    public CommandResponse deleteById(Long id) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(Constants.NOT_FOUND_CATEGORY);
                    return new NoSuchElementFoundException(Constants.NOT_FOUND_CATEGORY);
                });
        categoryRepository.delete(category);
        return CommandResponse.builder().id(id).build();
    }
}
