package com.github.yildizmy.service;

import com.github.yildizmy.dto.request.UnitRequest;
import com.github.yildizmy.dto.response.UnitResponse;
import com.github.yildizmy.model.Unit;
import com.github.yildizmy.common.filter.SearchRequest;
import com.github.yildizmy.exception.ElementAlreadyExistsException;
import com.github.yildizmy.exception.NoSuchElementFoundException;
import com.github.yildizmy.repository.UnitRepository;
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
 * Unit Test for UnitService methods
 */
@ExtendWith(MockitoExtension.class)
class UnitServiceTest {

    @InjectMocks
    private UnitService service;

    @Mock
    private UnitRepository unitRepository;

    @Captor
    private ArgumentCaptor<Unit> unitCaptor;

    /**
     * Method under test: {@link UnitService#findById(Long)}
     */
    @Test
    void findById_should_throw_NoSuchElementFoundException_when_UnitIsNotFound() {
        when(unitRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            service.findById(101L);
        });

        verify(unitRepository).findById(101L);
    }

    /**
     * Method under test: {@link UnitService#findById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/units.csv")
    void findById_should_return_UnitResponse_when_UnitIsFound(Long id, String name, Integer ordinal) {
        Unit unit = new Unit(id, name);

        when(unitRepository.findById(id)).thenReturn(Optional.of(unit));

        UnitResponse result = service.findById(id);

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        verify(unitRepository).findById(id);
    }

    /**
     * Method under test: {@link UnitService#findAll(SearchRequest)}
     */
    @Test
    void findAll_should_throw_NoSuchElementFoundException_when_NoUnitIsFound() {
        SearchRequest request = new SearchRequest();

        when(unitRepository.findAll((Specification<Unit>) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> service.findAll(request));

        verify(unitRepository).findAll((Specification<Unit>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link UnitService#findAll(SearchRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/units.csv")
    void findAll_should_return_UnitResponseList_when_UnitIsFound(Long id, String name, Integer ordinal) {
        Unit unit = new Unit(id, name);
        ArrayList<Unit> unitList = new ArrayList<>();
        unitList.add(unit);
        PageImpl<Unit> pageImpl = new PageImpl<>(unitList);

        when(unitRepository.findAll((Specification<Unit>) any(), (Pageable) any())).thenReturn(pageImpl);

        List<UnitResponse> result = service.findAll(new SearchRequest()).toList();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(name, result.get(0).getName());
        verify(unitRepository).findAll((Specification<Unit>) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link UnitService#create(UnitRequest)}
     */
    @Test
    void create_should_throw_ElementAlreadyExistsException_when_UnitExists() {
        UnitRequest request = new UnitRequest(101L, "Unit");

        when(unitRepository.existsByNameIgnoreCase(request.getName())).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> service.create(request));

        verify(unitRepository, never()).save(any());
    }

    /**
     * Method under test: {@link UnitService#create(UnitRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/units.csv")
    void create_should_return_CommandResponse_when_UnitNotExists(Long id, String name, Integer ordinal) {
        UnitRequest request = new UnitRequest(id, name);

        when(unitRepository.existsByNameIgnoreCase(request.getName())).thenReturn(false);

        service.create(request);
        verify(unitRepository).save(unitCaptor.capture());
        Unit capturedUnit = unitCaptor.getValue();

        assertEquals(capitalizeFully(name), capturedUnit.getName());
        verify(unitRepository).save(capturedUnit);
    }

    /**
     * Method under test: {@link UnitService#update(UnitRequest)}
     */
    @Test
    void update_should_throw_NoSuchElementFoundException_when_UnitNotExists() {
        UnitRequest request = new UnitRequest(101L, "Unit");

        when(unitRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.update(request));

        verify(unitRepository, never()).existsByNameIgnoreCase(any());
        verify(unitRepository, never()).save(any());
    }

    /**
     * Method under test: {@link UnitService#update(UnitRequest)}
     */
    @Test
    void update_should_throw_ElementAlreadyExistsException_when_UnitExists() {
        UnitRequest request = new UnitRequest(101L, "Unit");

        when(unitRepository.findById(request.getId())).thenReturn(Optional.of(new Unit()));
        when(unitRepository.existsByNameIgnoreCase(request.getName())).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> service.update(request));

        verify(unitRepository, never()).save(any());
    }

    /**
     * Method under test: {@link UnitService#update(UnitRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/units.csv")
    void update_should_return_CommandResponse_when_UnitNotExists(Long id, String name, Integer ordinal) {
        UnitRequest request = new UnitRequest(id, name);
        Unit unit = new Unit(id, name);

        when(unitRepository.findById(request.getId())).thenReturn(Optional.of(unit));
        when(unitRepository.existsByNameIgnoreCase(request.getName())).thenReturn(false);

        service.update(request);
        verify(unitRepository).save(unitCaptor.capture());
        Unit capturedUnit = unitCaptor.getValue();

        assertEquals(id, capturedUnit.getId());
        assertEquals(capitalizeFully(name), capturedUnit.getName());
        verify(unitRepository).save(capturedUnit);
    }

    /**
     * Method under test: {@link UnitService#deleteById(Long)}
     */
    @Test
    void deleteById_should_throw_NoSuchElementFoundException_when_UnitNotExists() {
        when(unitRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> service.deleteById(101L));

        verify(unitRepository, never()).delete(any());
    }

    /**
     * Method under test: {@link UnitService#deleteById(Long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/units.csv")
    void deleteById_should_return_CommandResponse_when_UnitIsFound(Long id, String name, Integer ordinal) {
        Unit unit = new Unit(id, name);

        when(unitRepository.findById(id)).thenReturn(Optional.of(unit));

        service.deleteById(id);
        verify(unitRepository).delete(unitCaptor.capture());
        Unit capturedUnit = unitCaptor.getValue();

        assertEquals(id, capturedUnit.getId());
        assertEquals(name, capturedUnit.getName());
        verify(unitRepository).delete(capturedUnit);
    }
}