package org.example.domain.publisher;

import org.example.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent>  {
    void publish(T domainEvent);
}
