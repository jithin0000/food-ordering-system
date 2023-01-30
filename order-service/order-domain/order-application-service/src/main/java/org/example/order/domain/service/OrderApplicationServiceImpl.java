package org.example.order.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.order.domain.service.dto.create.CreateOrderCommand;
import org.example.order.domain.service.dto.create.CreateOrderResponse;
import org.example.order.domain.service.dto.track.TrackOrderQuery;
import org.example.order.domain.service.dto.track.TrackOrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {
    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    public OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler, OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.createTrackOrder(trackOrderQuery);
    }
}
