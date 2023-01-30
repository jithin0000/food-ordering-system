package org.example.order.service.domain.service;

import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.events.OrderCancellEvent;
import org.example.order.service.domain.events.OrderCreatedEvent;
import org.example.order.service.domain.events.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndIntializeOrder(Order order, Restaurant restaurant);
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);
    OrderCancellEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void cancelOrder(Order order, List<String> failureMessages);
}
