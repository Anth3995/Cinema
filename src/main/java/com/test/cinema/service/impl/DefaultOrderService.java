package com.test.cinema.service.impl;

import com.test.cinema.dto.OrderDetailDto;
import com.test.cinema.dto.OrderDetailRequestDto;
import com.test.cinema.dto.OrderDto;
import com.test.cinema.entity.MovieEntity;
import com.test.cinema.entity.OrderEntity;
import com.test.cinema.exception.AlreadyExistsException;
import com.test.cinema.exception.ResourceNotFoundException;
import com.test.cinema.mapper.MovieMapper;
import com.test.cinema.mapper.OrderMapper;
import com.test.cinema.repository.OrderRepository;
import com.test.cinema.service.MovieService;
import com.test.cinema.service.OrderService;
import com.test.cinema.utils.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.test.cinema.repository.specification.OrderSpecification.hasId;
import static com.test.cinema.repository.specification.OrderSpecification.hasName;
import static com.test.cinema.repository.specification.OrderSpecification.nameContains;
import static com.test.cinema.repository.specification.OrderSpecification.priceContains;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {
    private final static String ORDER_NOT_FOUND_TEMPLATE = "Order with id %s was not found";

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Override
    public List<OrderDto> getAll(String search, Pageable pageable) {
        String unifySearch = SearchUtils.unifySearchParamSafe(search);
        Specification<OrderEntity> searchSpecification = where(nameContains(unifySearch))
                .or(priceContains(unifySearch));
        return orderRepository.findAll(searchSpecification, pageable)
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDetailDto getById(Long id) {
        return orderRepository.findOne(hasId(id))
                .map(orderMapper::toDetailDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND_TEMPLATE, id)));
    }

    @Override
    @Transactional
    public OrderDetailDto create(OrderDetailRequestDto orderDetailRequestDto) {
        OrderEntity entity = orderMapper.toEntity(orderDetailRequestDto);
        String name = entity.getName();
        validateName(name);
        List<MovieEntity> movieEntities = getMovieEntities(orderDetailRequestDto);
        entity.setMovies(movieEntities);
        orderRepository.save(entity);
        return orderMapper.toDetailDto(entity);
    }

    @Override
    @Transactional
    public OrderDetailDto update(Long id, OrderDetailRequestDto orderDetailRequestDto) {
        OrderEntity entity = orderRepository.findOne(hasId(id))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND_TEMPLATE, id)));
        String newName = orderDetailRequestDto.getName();
        if (!StringUtils.equals(entity.getName(), newName)) {
            validateName(newName);
        }
        List<MovieEntity> movieEntities = getMovieEntities(orderDetailRequestDto);
        entity.setMovies(movieEntities);
        orderMapper.updateEntityFromDto(orderDetailRequestDto, entity);
        orderRepository.saveAndFlush(entity);
        return orderMapper.toDetailDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!orderRepository.exists(hasId(id))) {
            throw new ResourceNotFoundException(String.format(ORDER_NOT_FOUND_TEMPLATE, id));
        }
        orderRepository.deleteById(id);
    }

    private void validateName(String name) {
        if (orderRepository.exists(hasName(name))) {
            throw new AlreadyExistsException("Order with that name already exists");
        }
    }

    private List<MovieEntity> getMovieEntities(OrderDetailRequestDto orderDetailRequestDto) {
        List<Long> movieIds = orderDetailRequestDto.getMovieIds();
        return movieService.getModelsByIds(movieIds).stream()
                .map(movieMapper::toEntity)
                .collect(Collectors.toList());
    }
}
