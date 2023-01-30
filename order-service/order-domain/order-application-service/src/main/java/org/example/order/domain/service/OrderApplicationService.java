package org.example.order.domain.service;

import org.example.order.domain.service.dto.create.CreateOrderCommand;
import org.example.order.domain.service.dto.create.CreateOrderResponse;
import org.example.order.domain.service.dto.track.TrackOrderQuery;
import org.example.order.domain.service.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
