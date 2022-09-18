package com.test.cinema.controller;

import com.test.cinema.dto.OrderDetailDto;
import com.test.cinema.dto.OrderDetailRequestDto;
import com.test.cinema.dto.OrderDto;
import com.test.cinema.meta.Endpoints;
import com.test.cinema.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoints.ORDERS)
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PageableAsQueryParam
    public List<OrderDto> getAll(
            @RequestParam(required = false) String search,
            @Parameter(hidden = true) Pageable pageable
    ) {
        return orderService.getAll(search, pageable);
    }

    @GetMapping("/{orderId}")
    public OrderDetailDto getById(@PathVariable("orderId") Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    public OrderDetailDto create(
            @Valid @RequestBody OrderDetailRequestDto orderDetailRequestDto
    ) {
        return orderService.create(orderDetailRequestDto);
    }

    @PutMapping("/{orderId}")
    public OrderDetailDto update(
            @PathVariable("orderId") Long id,
            @Valid @RequestBody OrderDetailRequestDto orderDetailRequestDto
    ) {
        return orderService.update(id, orderDetailRequestDto);
    }

    @DeleteMapping("/{orderId}")
    public void deleteById(@PathVariable("orderId") Long id) {
        orderService.deleteById(id);
    }
}
