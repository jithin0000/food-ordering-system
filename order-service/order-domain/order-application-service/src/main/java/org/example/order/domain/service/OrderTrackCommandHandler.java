package org.example.order.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.order.domain.service.dto.track.TrackOrderQuery;
import org.example.order.domain.service.dto.track.TrackOrderResponse;
import org.example.order.domain.service.mapper.OrderDataMapper;
import org.example.order.domain.service.ports.output.repository.OrderRepository;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.exception.OrderNotFoundException;
import org.example.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {
    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderTrackCommandHandler(OrderRepository orderRepository, OrderDataMapper orderDataMapper) {
        this.orderRepository = orderRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional(readOnly = true)
    public TrackOrderResponse createTrackOrder(TrackOrderQuery trackOrderQuery){
        Optional<Order> orderResult = orderRepository.findByTrackingId(new TrackingId(
                trackOrderQuery.getOrderTrackingId()
        ));
        if (orderResult.isEmpty()) {
            log.warn("could not found order with id : {} ",trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("could not found order with id "+trackOrderQuery.getOrderTrackingId());
        }

        return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
    }
}
