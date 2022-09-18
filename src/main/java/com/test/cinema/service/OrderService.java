package com.test.cinema.service;

import com.test.cinema.dto.OrderDetailDto;
import com.test.cinema.dto.OrderDetailRequestDto;
import com.test.cinema.dto.OrderDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAll(String search, Pageable pageable);

    OrderDetailDto getById(Long id);

    OrderDetailDto create(OrderDetailRequestDto orderDetailRequestDto);

    OrderDetailDto update(Long id, OrderDetailRequestDto orderDetailRequestDto);

    void deleteById(Long id);
}
