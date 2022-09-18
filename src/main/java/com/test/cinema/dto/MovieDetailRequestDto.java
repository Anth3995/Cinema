package com.test.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailRequestDto {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String director;
}
