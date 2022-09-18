package com.test.cinema.repository.specification;

import com.test.cinema.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<OrderEntity> hasName(String name) {
        return (order, cq, cb) -> cb.equal(order.get("name"), name);
    }

    public static Specification<OrderEntity> hasId(Long id) {
        return (order, cq, cb) -> cb.equal(order.get("id"), id);
    }

    public static Specification<OrderEntity> nameContains(String name) {
        return (order, cq, cb) -> cb.like(order.get("name"), "%" + name + "%");
    }

    public static Specification<OrderEntity> priceContains(String price) {
        return (order, cq, cb) -> cb.like(order.get("price").as(String.class), "%" + price + "%");
    }
}
