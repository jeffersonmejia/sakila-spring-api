package com.sakila.api.domain.model;

public record MostRentedFilm(Integer filmId, String title, Long timesRented) {
}
