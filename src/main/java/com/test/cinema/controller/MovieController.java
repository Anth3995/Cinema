package com.test.cinema.controller;

import com.test.cinema.dto.MovieDetailDto;
import com.test.cinema.dto.MovieDetailRequestDto;
import com.test.cinema.dto.MovieDto;
import com.test.cinema.meta.Endpoints;
import com.test.cinema.service.MovieService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoints.MOVIES)
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    @PageableAsQueryParam
    public List<MovieDto> getAll(
            @RequestParam(required = false) String search,
            @Parameter(hidden = true) Pageable pageable
    ) {
        return movieService.getAll(search, pageable);
    }

    @GetMapping("/{movieId}")
    public MovieDetailDto getById(@PathVariable("movieId") Long id) {
        return movieService.getById(id);
    }

    @PostMapping
    public MovieDetailDto create(
            @Valid @RequestBody MovieDetailRequestDto MovieDetailRequestDto
    ) {
        return movieService.create(MovieDetailRequestDto);
    }

    @PutMapping("/{movieId}")
    public MovieDetailDto update(
            @PathVariable("movieId") Long id,
            @Valid @RequestBody MovieDetailRequestDto MovieDetailRequestDto
    ) {
        return movieService.update(id, MovieDetailRequestDto);
    }

    @DeleteMapping("/{movieId}")
    public void deleteById(@PathVariable("movieId") Long id) {
        movieService.deleteById(id);
    }
}
