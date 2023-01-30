package org.example.order.domain.service.ports.output.message.publisher.payment;

import org.example.domain.publisher.DomainEventPublisher;
import org.example.order.service.domain.events.OrderCancellEvent;
import org.example.order.service.domain.events.OrderCreatedEvent;

public interface OrderCancelPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancellEvent> {
}
