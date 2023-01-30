package org.example.order.service.application.rest;

import lombok.extern.slf4j.Slf4j;
import org.example.order.domain.service.OrderApplicationService;
import org.example.order.domain.service.dto.create.CreateOrderCommand;
import org.example.order.domain.service.dto.create.CreateOrderResponse;
import org.example.order.domain.service.dto.track.TrackOrderQuery;
import org.example.order.domain.service.dto.track.TrackOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {
    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderCommand createOrderCommand)
    {
        log.info("create order for customer : {} at restaurant: {} ", createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with tracking id : {} ", createOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }
    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> trackOrder(@PathVariable UUID trackingId)
    {
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(
                TrackOrderQuery.builder().orderTrackingId(trackingId).build()
        );
        log.info("Returning order status with tracking id: {} ", trackOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(trackOrderResponse);
    }













}
