package com.test.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Long id;
    private String name;
    private Long price;
    private List<MovieDetailDto> movies = new ArrayList<>();
}
