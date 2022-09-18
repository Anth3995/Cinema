package com.test.cinema.service.impl;

import com.test.cinema.dto.MovieDetailDto;
import com.test.cinema.dto.MovieDetailRequestDto;
import com.test.cinema.dto.MovieDto;
import com.test.cinema.entity.MovieEntity;
import com.test.cinema.exception.AlreadyExistsException;
import com.test.cinema.exception.ResourceNotFoundException;
import com.test.cinema.mapper.MovieMapper;
import com.test.cinema.model.MovieModel;
import com.test.cinema.repository.MovieRepository;
import com.test.cinema.service.MovieService;
import com.test.cinema.utils.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.test.cinema.repository.specification.MovieSpecification.descriptionContains;
import static com.test.cinema.repository.specification.MovieSpecification.directorContains;
import static com.test.cinema.repository.specification.MovieSpecification.hasId;
import static com.test.cinema.repository.specification.MovieSpecification.hasName;
import static com.test.cinema.repository.specification.MovieSpecification.nameContains;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final static String MOVIE_NOT_FOUND_TEMPLATE = "Movie with id %s was not found";

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public List<MovieDto> getAll(String search, Pageable pageable) {
        String unifySearch = SearchUtils.unifySearchParamSafe(search);
        Specification<MovieEntity> searchSpecification = where(nameContains(unifySearch))
                .or(descriptionContains(unifySearch))
                .or(directorContains(unifySearch));
        return movieRepository.findAll(searchSpecification, pageable)
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public MovieDetailDto getById(Long id) {
        return movieRepository.findOne(hasId(id))
                .map(movieMapper::toDetailDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MOVIE_NOT_FOUND_TEMPLATE, id)));
    }

    @Override
    @Transactional
    public MovieDetailDto create(MovieDetailRequestDto movieDetailRequestDto) {
        MovieEntity entity = movieMapper.toEntity(movieDetailRequestDto);
        String name = entity.getName();
        validateName(name);
        movieRepository.save(entity);
        return movieMapper.toDetailDto(entity);
    }

    @Override
    @Transactional
    public MovieDetailDto update(Long id, MovieDetailRequestDto movieDetailRequestDto) {
        MovieEntity entity = movieRepository.findOne(hasId(id))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MOVIE_NOT_FOUND_TEMPLATE, id)));
        String newName = movieDetailRequestDto.getName();
        if (!StringUtils.equals(entity.getName(), newName)) {
            validateName(newName);
        }
        movieMapper.updateEntityFromDto(movieDetailRequestDto, entity);
        movieRepository.saveAndFlush(entity);
        return movieMapper.toDetailDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!movieRepository.exists(hasId(id))) {
            throw new ResourceNotFoundException(String.format(MOVIE_NOT_FOUND_TEMPLATE, id));
        }
        movieRepository.deleteById(id);
    }

    @Override
    public MovieModel getModelById(Long id) {
        return movieRepository.findOne(hasId(id))
                .map(movieMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MOVIE_NOT_FOUND_TEMPLATE, id)));
    }

    @Override
    public List<MovieModel> getModelsByIds(List<Long> ids) {
        return ids.stream()
                .map(this::getModelById)
                .collect(Collectors.toList());
    }

    private void validateName(String name) {
        if (movieRepository.exists(hasName(name))) {
            throw new AlreadyExistsException("Movie with that name already exists");
        }
    }
}
