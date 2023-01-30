package org.example.order.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.order.domain.service.dto.create.CreateOrderCommand;
import org.example.order.domain.service.dto.create.CreateOrderResponse;
import org.example.order.domain.service.mapper.OrderDataMapper;
import org.example.order.domain.service.ports.output.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import org.example.order.service.domain.events.OrderCreatedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {
    private final OrderDataMapper orderDataMapper;
    private final OrderCreateHelper orderCreateHelper;
    private final OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderDataMapper orderDataMapper, OrderCreateHelper orderCreateHelper, OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher) {
        this.orderDataMapper = orderDataMapper;
        this.orderCreateHelper = orderCreateHelper;
        this.orderCreatePaymentRequestMessagePublisher = orderCreatePaymentRequestMessagePublisher;
    }


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id {} ", orderCreatedEvent.getOrder().getId().getValue());
        orderCreatePaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(
                orderCreatedEvent.getOrder(),
                "Order created successfully"
        );
    }

}
