package com.test.cinema.mapper;

import com.test.cinema.dto.OrderDetailDto;
import com.test.cinema.dto.OrderDetailRequestDto;
import com.test.cinema.dto.OrderDto;
import com.test.cinema.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface OrderMapper {
    OrderDto toDto(OrderEntity orderEntity);

    OrderDetailDto toDetailDto(OrderEntity orderEntity);

    OrderEntity toEntity(OrderDetailRequestDto detailRequestDto);

    void updateEntityFromDto(OrderDetailRequestDto orderDetailRequestDto, @MappingTarget OrderEntity entity);
}
