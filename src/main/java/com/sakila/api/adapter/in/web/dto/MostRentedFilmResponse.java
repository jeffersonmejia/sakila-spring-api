package com.sakila.api.adapter.in.web.dto;

public record MostRentedFilmResponse(Integer filmId, String title, Long timesRented) {
}
