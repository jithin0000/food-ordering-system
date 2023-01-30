package org.example.order.service.domain.events;

import org.example.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancellEvent extends OrderEvent {

    public OrderCancellEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
