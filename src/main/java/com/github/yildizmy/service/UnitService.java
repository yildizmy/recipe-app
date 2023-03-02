package com.github.yildizmy.service;

import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.common.filter.SearchSpecification;
import com.github.yildizmy.dto.mapper.UnitRequestMapper;
import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.dto.response.CommandResponse;
import com.github.yildizmy.dto.response.UnitResponse;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.model.Unit;
import com.github.yildizmy.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.yildizmy.common.Constants.*;
import static org.apache.commons.text.WordUtils.capitalizeFully;

/**
 * Service used for adding, updating, removing and fetching units
 */
@Slf4j(topic = "UnitService")
@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final UnitRequestMapper unitRequestMapper;

    /**
     * Fetches a unit by the given id
     *
     * @param id
     * @return
     */
    public UnitResponse findById(Long id) {
        return unitRepository.findById(id)
                .map(UnitResponse::new)
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_UNIT);
                    return new NoSuchElementFoundException(NOT_FOUND_UNIT);
                });
    }

    /**
     * Fetches all units based on the given filter parameters
     *
     * @param request
     * @return Paginated unit data
     */
    @Transactional(readOnly = true)
    public Page<UnitResponse> findAll(SearchRequest request) {
        final SearchSpecification<Unit> specification = new SearchSpecification<>(request);
        final Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        final Page<UnitResponse> units = unitRepository.findAll(specification, pageable)
                .map(UnitResponse::new);
        if (units.isEmpty()) {
            log.error(NOT_FOUND_RECORD);
            throw new NoSuchElementFoundException(NOT_FOUND_RECORD);
        }
        return units;
    }

    /**
     * Creates a new unit using the given request parameters
     *
     * @param request
     * @return
     */
    public CommandResponse create(UnitRequest request) {
        if (unitRepository.existsByNameIgnoreCase(request.getName())) {
            log.error(ALREADY_EXISTS_UNIT);
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_UNIT);
        }
        final Unit unit = unitRequestMapper.toEntity(request);
        unitRepository.save(unit);
        return CommandResponse.builder().id(unit.getId()).build();
    }

    /**
     * Updates unit using the given request parameters
     *
     * @param request
     * @return
     */
    public CommandResponse update(UnitRequest request) {
        final Unit unit = unitRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_UNIT);
                    return new NoSuchElementFoundException(NOT_FOUND_UNIT);
                });

        if (unitRepository.existsByNameIgnoreCase(request.getName())) {
            log.error(ALREADY_EXISTS_UNIT);
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_UNIT);
        }
        unit.setName(capitalizeFully(request.getName()));
        unitRepository.save(unit);
        return CommandResponse.builder().id(unit.getId()).build();
    }

    /**
     * Deletes unit by the given id
     *
     * @param id
     * @return
     */
    public void deleteById(Long id) {
        final Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(NOT_FOUND_UNIT);
                    return new NoSuchElementFoundException(NOT_FOUND_UNIT);
                });
        unitRepository.delete(unit);
    }
}
