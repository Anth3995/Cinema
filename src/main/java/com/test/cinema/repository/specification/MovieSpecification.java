package com.test.cinema.repository.specification;

import com.test.cinema.entity.MovieEntity;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {
    public static Specification<MovieEntity> hasName(String name) {
        return (movie, cq, cb) -> cb.equal(movie.get("name"), name);
    }

    public static Specification<MovieEntity> hasId(Long id) {
        return (movie, cq, cb) -> cb.equal(movie.get("id"), id);
    }

    public static Specification<MovieEntity> nameContains(String name) {
        return (movie, cq, cb) -> cb.like(movie.get("name"), "%" + name + "%");
    }

    public static Specification<MovieEntity> descriptionContains(String description) {
        return (movie, cq, cb) -> cb.like(movie.get("description"), "%" + description + "%");
    }

    public static Specification<MovieEntity> directorContains(String director) {
        return (movie, cq, cb) -> cb.like(movie.get("director"), "%" + director + "%");
    }
}
