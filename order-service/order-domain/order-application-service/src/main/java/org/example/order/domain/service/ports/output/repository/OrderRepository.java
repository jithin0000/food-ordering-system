package org.example.order.domain.service.ports.output.repository;

import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByTrackingId(TrackingId trackingId);
}
