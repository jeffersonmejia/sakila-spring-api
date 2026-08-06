package com.sakila.api.adapter.in.web.dto;

import java.util.List;

public record FilmDetailResponse(FilmResponse film, List<ActorResponse> actors, List<CategoryResponse> categories) {
}
