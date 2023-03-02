package com.github.yildizmy.service;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.common.filter.SearchSpecification;
import com.github.yildizmy.dto.mapper.IngredientRequestMapper;
import com.github.yildizmy.dto.request.IngredientRequest;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.dto.response.IngredientResponse;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.model.Ingredient;
import com.github.yildizmy.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.yildizmy.common.Constants.*;
import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Service used for adding, updating, removing and fetching ingredients
 */
@Slf4j(topic = "IngredientService")
@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientRequestMapper ingredientRequestMapper;

    /**
     * Fetches an ingredient by the given id
     *
     * @param id
     * @return
     */
    public IngredientResponse findById(Long id) {
        return ingredientRepository.findById(id)
                .map(IngredientResponse::new)
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_INGREDIENT);
                    return new NoSuchElementFoundException(NOT_FOUND_INGREDIENT);
                });
    }

    /**
     * Fetches all ingredients based on the given filter parameters
     *
     * @param request
     * @return Paginated ingredient data
     */
    @Transactional(readOnly = true)
    public Page<IngredientResponse> findAll(SearchRequest request) {
        final SearchSpecification<Ingredient> specification = new SearchSpecification<>(request);
        final Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        final Page<IngredientResponse> ingredients = ingredientRepository.findAll(specification, pageable)
                .map(IngredientResponse::new);
        if (ingredients.isEmpty()) {
            log.error(NOT_FOUND_RECORD);
            throw new NoSuchElementFoundException(NOT_FOUND_RECORD);
        }
        return ingredients;
    }

    /**
     * Creates a new ingredient using the given request parameters
     *
     * @param request
     * @return
     */
    public CommandResponse create(IngredientRequest request) {
        if (ingredientRepository.existsByNameIgnoreCase(request.getName())) {
            log.error(ALREADY_EXISTS_INGREDIENT);
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_INGREDIENT);
        }
        final Ingredient ingredient = ingredientRequestMapper.toEntity(request);
        ingredientRepository.save(ingredient);
        return CommandResponse.builder().id(ingredient.getId()).build();
    }

    /**
     * Updates ingredient using the given request parameters
     *
     * @param request
     * @return
     */
    public CommandResponse update(IngredientRequest request) {
        final Ingredient ingredient = ingredientRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_INGREDIENT);
                    return new NoSuchElementFoundException(NOT_FOUND_INGREDIENT);
                });

        if (ingredientRepository.existsByNameIgnoreCase(request.getName())) {
            log.error(ALREADY_EXISTS_INGREDIENT);
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_INGREDIENT);
        }
        ingredient.setName(capitalizeFully(request.getName()));
        ingredientRepository.save(ingredient);
        return CommandResponse.builder().id(ingredient.getId()).build();
    }

    /**
     * Deletes ingredient by the given id
     *
     * @param id
     * @return
     */
    public void deleteById(Long id) {
        final Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_INGREDIENT);
                    return new NoSuchElementFoundException(NOT_FOUND_INGREDIENT);
                });
        ingredientRepository.delete(ingredient);
    }
}
