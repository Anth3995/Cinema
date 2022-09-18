package com.test.cinema.service;

import com.test.cinema.dto.MovieDetailDto;
import com.test.cinema.dto.MovieDetailRequestDto;
import com.test.cinema.dto.MovieDto;
import com.test.cinema.model.MovieModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    List<MovieDto> getAll(String search, Pageable pageable);

    MovieDetailDto getById(Long id);

    MovieDetailDto create(MovieDetailRequestDto movieDetailRequestDto);

    MovieDetailDto update(Long id, MovieDetailRequestDto movieDetailRequestDto);

    void deleteById(Long id);

    MovieModel getModelById(Long id);

    List<MovieModel> getModelsByIds(List<Long> ids);
}
