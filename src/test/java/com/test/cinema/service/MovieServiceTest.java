package com.test.cinema.service;

import com.test.cinema.dto.MovieDetailDto;
import com.test.cinema.dto.MovieDetailRequestDto;
import com.test.cinema.entity.MovieEntity;
import com.test.cinema.exception.AlreadyExistsException;
import com.test.cinema.exception.ResourceNotFoundException;
import com.test.cinema.mapper.MovieMapper;
import com.test.cinema.repository.MovieRepository;
import com.test.cinema.service.impl.DefaultMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
class MovieServiceTest {
    private final MovieMapper movieMapper = Mappers.getMapper(MovieMapper.class);

    @MockBean
    private MovieRepository movieRepository;

    private MovieService movieService;

    @BeforeEach
    void initMockService() {
        movieService = new DefaultMovieService(movieRepository, movieMapper);
    }

    @Test
    void shouldCreateAndReturnMovieDetailDto() {
        MovieDetailRequestDto movieDetailRequestDto = new MovieDetailRequestDto(
                "mockName",
                "mockDesc",
                "mockDirector"
        );

        when(movieRepository.exists(
                ArgumentMatchers.<Specification<MovieEntity>>any()
        )).thenReturn(false);

        MovieDetailDto result = movieService.create(movieDetailRequestDto);
        assertEquals(movieDetailRequestDto.getName(), result.getName());
        assertEquals(movieDetailRequestDto.getDescription(), result.getDescription());
        assertEquals(movieDetailRequestDto.getDirector(), result.getDirector());
        verify(movieRepository, times(1)).save(any(MovieEntity.class));
    }

    @Test
    void shouldThrowAlreadyExistsExceptionWhenCreateWithNotUniqueName() {
        MovieDetailRequestDto movieDetailRequestDto = new MovieDetailRequestDto(
                "mockName",
                "mockDesc",
                "mockDirector"
        );

        when(movieRepository.exists(
                ArgumentMatchers.<Specification<MovieEntity>>any()
        )).thenReturn(true);

        assertThatThrownBy(
                () -> movieService.create(movieDetailRequestDto)
        ).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void shouldReturnMovieDetailDtoById() {
        MovieEntity entity = new MovieEntity(
                1L,
                "mockName",
                null,
                "mockDirector",
                Collections.emptyList()
        );

        when(movieRepository.findOne(ArgumentMatchers.<Specification<MovieEntity>>any()))
                .thenReturn(Optional.of(entity));

        MovieDetailDto result = movieService.getById(entity.getId());
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertNull(entity.getDescription());
        assertEquals(entity.getDirector(), result.getDirector());
    }

    @Test
    void shouldDeleteById() {
        Long id = 1L;

        when(movieRepository.exists(
                ArgumentMatchers.<Specification<MovieEntity>>any()
        )).thenReturn(true);

        movieService.deleteById(id);
        verify(movieRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteByNotExistsId() {
        Long id = 1L;

        when(movieRepository.exists(
                ArgumentMatchers.<Specification<MovieEntity>>any()
        )).thenReturn(false);

        assertThatThrownBy(
                () -> movieService.deleteById(id)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdateAndReturnMovieDetailDto() {
        Long id = 1L;
        MovieDetailRequestDto movieDetailRequestDto = new MovieDetailRequestDto(
                "mockName",
                "mockDesc",
                "mockDirector"
        );
        MovieEntity entity = new MovieEntity(
                1L,
                "oldMockName",
                null,
                "mockDirector",
                Collections.emptyList()
        );

        when(movieRepository.exists(
                ArgumentMatchers.<Specification<MovieEntity>>any()
        )).thenReturn(false);
        when(movieRepository.findOne(ArgumentMatchers.<Specification<MovieEntity>>any()))
                .thenReturn(Optional.of(entity));

        MovieDetailDto movieDetailDto = movieService.update(id, movieDetailRequestDto);
        assertEquals(movieDetailRequestDto.getName(), movieDetailDto.getName());
        assertEquals(movieDetailRequestDto.getDescription(), movieDetailDto.getDescription());
        assertEquals(movieDetailRequestDto.getDirector(), movieDetailDto.getDirector());
        verify(movieRepository, times(1)).saveAndFlush(any(MovieEntity.class));
    }

    @Test
    void shouldThrowAlreadyExistsExceptionWhenUpdateWithNotUniqueName() {
        Long id = 1L;
        MovieDetailRequestDto movieDetailRequestDto = new MovieDetailRequestDto(
                "mockName",
                "mockDesc",
                "mockDirector"
        );
        MovieEntity entity = new MovieEntity(
                1L,
                "oldMockName",
                null,
                "mockDirector",
                Collections.emptyList()
        );

        when(movieRepository.exists(
                ArgumentMatchers.<Specification<MovieEntity>>any()
        )).thenReturn(true);
        when(movieRepository.findOne(ArgumentMatchers.<Specification<MovieEntity>>any()))
                .thenReturn(Optional.of(entity));

        assertThatThrownBy(
                () -> movieService.update(id, movieDetailRequestDto)
        ).isInstanceOf(AlreadyExistsException.class);
    }
}
