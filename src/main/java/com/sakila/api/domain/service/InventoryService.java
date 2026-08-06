package com.sakila.api.domain.service;

import org.springframework.stereotype.Service;

import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Availability;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.port.in.InventoryQuery;
import com.sakila.api.domain.port.out.FilmRepository;
import com.sakila.api.domain.port.out.InventoryRepository;

@Service
public class InventoryService implements InventoryQuery {

    private final InventoryRepository inventoryRepository;
    private final FilmRepository filmRepository;

    public InventoryService(InventoryRepository inventoryRepository, FilmRepository filmRepository) {
        this.inventoryRepository = inventoryRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public PageResult<Inventory> listInventory(PageQuery query) {
        return inventoryRepository.findAll(query);
    }

    @Override
    public Inventory getInventory(Integer id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Copia no encontrada"));
    }

    @Override
    public PageResult<Inventory> inventoryByFilm(Integer filmId, PageQuery query) {
        filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Película no encontrada"));
        return inventoryRepository.findByFilmId(filmId, query);
    }

    @Override
    public Availability availability(Integer filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Película no encontrada"));
        long total = inventoryRepository.countByFilmId(filmId);
        long available = inventoryRepository.findAvailableByFilmId(filmId).size();
        return new Availability(film, total, available);
    }
}
