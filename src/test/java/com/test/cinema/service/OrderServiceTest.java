package com.test.cinema.service;

import com.test.cinema.dto.MovieDetailDto;
import com.test.cinema.dto.OrderDetailDto;
import com.test.cinema.dto.OrderDetailRequestDto;
import com.test.cinema.entity.OrderEntity;
import com.test.cinema.exception.AlreadyExistsException;
import com.test.cinema.exception.ResourceNotFoundException;
import com.test.cinema.mapper.MovieMapper;
import com.test.cinema.mapper.OrderMapper;
import com.test.cinema.model.MovieModel;
import com.test.cinema.repository.OrderRepository;
import com.test.cinema.service.impl.DefaultOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
class OrderServiceTest {
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    private final MovieMapper movieMapper = Mappers.getMapper(MovieMapper.class);

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private MovieService movieService;
    private OrderService orderService;

    @BeforeEach
    void initMockService() {
        orderService = new DefaultOrderService(orderRepository, orderMapper, movieService, movieMapper);
    }

    @Test
    void shouldCreateAndReturnMovieDetailDto() {
        OrderDetailRequestDto orderDetailRequestDto = new OrderDetailRequestDto(
                "mockName",
                100L,
                List.of(1L, 2L, 3L)
        );
        List<MovieModel> movieModels = new ArrayList<>();
        movieModels.add(new MovieModel(1L, "mock1", null, "director1"));
        movieModels.add(new MovieModel(2L, "mock2", null, "director2"));
        movieModels.add(new MovieModel(3L, "mock3", null, "director3"));
        List<MovieDetailDto> expectedMovieDetailDtos = new ArrayList<>();
        expectedMovieDetailDtos.add(new MovieDetailDto(1L, "mock1", null, "director1"));
        expectedMovieDetailDtos.add(new MovieDetailDto(2L, "mock2", null, "director2"));
        expectedMovieDetailDtos.add(new MovieDetailDto(3L, "mock3", null, "director3"));

        when(orderRepository.exists(
                ArgumentMatchers.<Specification<OrderEntity>>any()
        )).thenReturn(false);
        when(movieService.getModelsByIds(orderDetailRequestDto.getMovieIds())).thenReturn(movieModels);

        OrderDetailDto result = orderService.create(orderDetailRequestDto);
        assertEquals(orderDetailRequestDto.getName(), result.getName());
        assertEquals(orderDetailRequestDto.getPrice(), result.getPrice());
        assertEquals(result.getMovies(), expectedMovieDetailDtos);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void shouldThrowAlreadyExistsExceptionWhenCreateWithNotUniqueName() {
        OrderDetailRequestDto orderDetailRequestDto = new OrderDetailRequestDto(
                "mockName",
                100L,
                List.of(1L, 2L, 3L)
        );

        when(orderRepository.exists(
                ArgumentMatchers.<Specification<OrderEntity>>any()
        )).thenReturn(true);

        assertThatThrownBy(
                () -> orderService.create(orderDetailRequestDto)
        ).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void shouldReturnMovieDetailDtoById() {
        OrderEntity entity = new OrderEntity(
                1L,
                "mockName",
                100L,
                Collections.emptyList()
        );

        when(orderRepository.findOne(ArgumentMatchers.<Specification<OrderEntity>>any()))
                .thenReturn(Optional.of(entity));

        OrderDetailDto result = orderService.getById(entity.getId());
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getPrice(), result.getPrice());
    }

    @Test
    void shouldDeleteById() {
        Long id = 1L;

        when(orderRepository.exists(
                ArgumentMatchers.<Specification<OrderEntity>>any()
        )).thenReturn(true);

        orderService.deleteById(id);
        verify(orderRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteByNotExistsId() {
        Long id = 1L;

        when(orderRepository.exists(
                ArgumentMatchers.<Specification<OrderEntity>>any()
        )).thenReturn(false);

        assertThatThrownBy(
                () -> orderService.deleteById(id)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdateAndReturnMovieDetailDto() {
        Long id = 1L;
        OrderDetailRequestDto orderDetailRequestDto = new OrderDetailRequestDto(
                "mockName",
                100L,
                List.of(1L, 2L, 3L)
        );
        OrderEntity entity = new OrderEntity(
                1L,
                "oldMockName",
                12L,
                Collections.emptyList()
        );
        List<MovieModel> movieModels = new ArrayList<>();
        movieModels.add(new MovieModel(1L, "mock1", null, "director1"));
        movieModels.add(new MovieModel(2L, "mock2", null, "director2"));
        movieModels.add(new MovieModel(3L, "mock3", null, "director3"));
        List<MovieDetailDto> expectedMovieDetailDtos = new ArrayList<>();
        expectedMovieDetailDtos.add(new MovieDetailDto(1L, "mock1", null, "director1"));
        expectedMovieDetailDtos.add(new MovieDetailDto(2L, "mock2", null, "director2"));
        expectedMovieDetailDtos.add(new MovieDetailDto(3L, "mock3", null, "director3"));

        when(orderRepository.exists(
                ArgumentMatchers.<Specification<OrderEntity>>any()
        )).thenReturn(false);
        when(orderRepository.findOne(ArgumentMatchers.<Specification<OrderEntity>>any()))
                .thenReturn(Optional.of(entity));
        when(movieService.getModelsByIds(orderDetailRequestDto.getMovieIds())).thenReturn(movieModels);

        OrderDetailDto orderDetailDto = orderService.update(id, orderDetailRequestDto);
        assertEquals(orderDetailRequestDto.getName(), orderDetailDto.getName());
        assertEquals(orderDetailRequestDto.getPrice(), orderDetailDto.getPrice());
        assertEquals(expectedMovieDetailDtos, orderDetailDto.getMovies());
        verify(orderRepository, times(1)).saveAndFlush(any(OrderEntity.class));
    }

    @Test
    void shouldThrowAlreadyExistsExceptionWhenUpdateWithNotUniqueName() {
        Long id = 1L;
        OrderDetailRequestDto orderDetailRequestDto = new OrderDetailRequestDto(
                "mockName",
                100L,
                List.of(1L, 2L, 3L)
        );
        OrderEntity entity = new OrderEntity(
                1L,
                "oldMockName",
                12L,
                Collections.emptyList()
        );

        when(orderRepository.exists(
                ArgumentMatchers.<Specification<OrderEntity>>any()
        )).thenReturn(true);
        when(orderRepository.findOne(ArgumentMatchers.<Specification<OrderEntity>>any()))
                .thenReturn(Optional.of(entity));

        assertThatThrownBy(
                () -> orderService.update(id, orderDetailRequestDto)
        ).isInstanceOf(AlreadyExistsException.class);
    }
}
