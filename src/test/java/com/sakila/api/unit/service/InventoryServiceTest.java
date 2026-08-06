package com.sakila.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Availability;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.Language;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.port.out.FilmRepository;
import com.sakila.api.domain.port.out.InventoryRepository;
import com.sakila.api.domain.service.InventoryService;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private final PageQuery query = PageQuery.of(0, 20, "id", SortDirection.ASC);
    private final Film film = new Film(1, "ACADEMY", "desc", 2006, new Language(1, "English"),
            (short) 6, BigDecimal.ONE, (short) 86, BigDecimal.TEN, "PG");
    private final Inventory inventory = new Inventory(1, 1, "ACADEMY", 1, true);

    @Test
    void listInventoryDelegates() {
        PageResult<Inventory> page = PageResult.of(List.of(inventory), 0, 20, 1);
        when(inventoryRepository.findAll(query)).thenReturn(page);

        PageResult<Inventory> result = inventoryService.listInventory(query);

        assertSame(page, result);
    }

    @Test
    void getInventoryReturnsExisting() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));

        Inventory result = inventoryService.getInventory(1);

        assertSame(inventory, result);
    }

    @Test
    void getInventoryThrowsWhenMissing() {
        when(inventoryRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> inventoryService.getInventory(999));

        assertEquals("Copia no encontrada", ex.getMessage());
    }

    @Test
    void inventoryByFilmDelegatesWhenFilmExists() {
        PageResult<Inventory> page = PageResult.of(List.of(inventory), 0, 20, 1);
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(inventoryRepository.findByFilmId(1, query)).thenReturn(page);

        PageResult<Inventory> result = inventoryService.inventoryByFilm(1, query);

        assertSame(page, result);
    }

    @Test
    void inventoryByFilmThrowsWhenFilmMissing() {
        when(filmRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> inventoryService.inventoryByFilm(999, query));

        assertEquals("Película no encontrada", ex.getMessage());
        verify(inventoryRepository, never()).findByFilmId(999, query);
    }

    @Test
    void availabilityComputesCounts() {
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(inventoryRepository.countByFilmId(1)).thenReturn(5L);
        when(inventoryRepository.findAvailableByFilmId(1)).thenReturn(List.of(inventory, inventory));

        Availability result = inventoryService.availability(1);

        assertSame(film, result.film());
        assertEquals(5L, result.totalCopies());
        assertEquals(2L, result.availableCopies());
    }

    @Test
    void availabilityThrowsWhenFilmMissing() {
        when(filmRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> inventoryService.availability(999));

        assertEquals("Película no encontrada", ex.getMessage());
        verify(inventoryRepository, never()).countByFilmId(999);
    }
}
