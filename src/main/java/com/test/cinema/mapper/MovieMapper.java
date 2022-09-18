package com.test.cinema.mapper;

import com.test.cinema.dto.MovieDetailDto;
import com.test.cinema.dto.MovieDetailRequestDto;
import com.test.cinema.dto.MovieDto;
import com.test.cinema.entity.MovieEntity;
import com.test.cinema.model.MovieModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface MovieMapper {
    MovieEntity toEntity(MovieDetailRequestDto detailRequestDto);

    MovieDetailDto toDetailDto(MovieEntity save);

    MovieDto toDto(MovieEntity entity);

    void updateEntityFromDto(MovieDetailRequestDto detailRequestDto, @MappingTarget MovieEntity entity);

    MovieModel toModel(MovieEntity entity);

    MovieEntity toEntity(MovieModel model);
}
