package com.test.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDto {
    @NotBlank
    private String name;
    @Positive
    private Integer price;
    @Size(min = 1, max = 5, message = "Order can to has no greater than 5 movies")
    @NotEmpty
    private List<Long> movieIds = new ArrayList<>();
}
