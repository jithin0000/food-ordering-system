package org.example.order.service.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.Product;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.events.OrderCancellEvent;
import org.example.order.service.domain.events.OrderCreatedEvent;
import org.example.order.service.domain.events.OrderPaidEvent;
import org.example.order.service.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndIntializeOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order,restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated ", order.getId().getValue());

        return new OrderCreatedEvent(order, ZonedDateTime.now(
                ZoneId.of(UTC)
        ));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid ", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved ", order.getId().getValue());
    }

    @Override
    public OrderCancellEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order with id: {} is cancel started ", order.getId().getValue());
        return new OrderCancellEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled ", order.getId().getValue());
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {

        order.getOrderItems().forEach( orderItem -> restaurant.getProducts().forEach(product -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(product))
            {
                currentProduct.updateWithConfirmedNameAndPrice(product.getName(), product.getPrice());
            }
        }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restuarant with id "+restaurant.getId().getValue() + " is not active");
        }
    }

}
