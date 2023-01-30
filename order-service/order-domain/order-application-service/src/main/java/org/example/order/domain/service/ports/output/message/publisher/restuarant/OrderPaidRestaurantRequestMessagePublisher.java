package org.example.order.domain.service.ports.output.message.publisher.restuarant;

import org.example.domain.publisher.DomainEventPublisher;
import org.example.order.service.domain.events.OrderCreatedEvent;
import org.example.order.service.domain.events.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
