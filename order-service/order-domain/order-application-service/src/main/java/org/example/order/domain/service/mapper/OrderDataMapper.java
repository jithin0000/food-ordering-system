package org.example.order.domain.service.mapper;

import org.example.domain.valueobject.CustomerId;
import org.example.domain.valueobject.Money;
import org.example.domain.valueobject.ProductId;
import org.example.domain.valueobject.RestaurantId;
import org.example.order.domain.service.dto.create.CreateOrderCommand;
import org.example.order.domain.service.dto.create.CreateOrderResponse;
import org.example.order.domain.service.dto.create.OrderAddress;
import org.example.order.domain.service.dto.track.TrackOrderResponse;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.OrderItem;
import org.example.order.service.domain.entity.Product;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrdreCommandToRestaurant(CreateOrderCommand createOrderCommand)
    {
       return Restaurant.Builder
               .builder().id(new RestaurantId(createOrderCommand.getRestaurantId()))
               .products(createOrderCommand.getItems().stream()
                       .map(orderItem -> new Product(new ProductId(orderItem.getProductId())))
                       .collect(Collectors.toList())
               ).build();
    }
    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand)
    {
        return Order.Builder.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAdress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .orderItems(orderItemsToOrderItemEntity(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message)
    {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }
    public TrackOrderResponse orderToTrackOrderResponse(Order order)
    {
        return TrackOrderResponse
                .builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntity(List<org.example.order.domain.service.dto.create.OrderItem> items) {
        return items.stream().map(
                orderItem -> OrderItem.Builder
                        .builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubTotal()))
                        .build()
        ).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity()
        );

    }

}