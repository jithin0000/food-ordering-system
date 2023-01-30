package org.example.order.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.exception.DomainException;
import org.example.order.domain.service.dto.create.CreateOrderCommand;
import org.example.order.domain.service.mapper.OrderDataMapper;
import org.example.order.domain.service.ports.output.repository.CustomerRepository;
import org.example.order.domain.service.ports.output.repository.OrderRepository;
import org.example.order.domain.service.ports.output.repository.RestaurantRepository;
import org.example.order.service.domain.entity.Customer;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.events.OrderCreatedEvent;
import org.example.order.service.domain.exception.OrderDomainException;
import org.example.order.service.domain.service.OrderDomainService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             RestaurantRepository restaurantRepository,
                             OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand)
    {
        checkCustomerExist(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndIntializeOrder(order, restaurant);
        saveOrder(order);
        log.info("Order is created with id {} ", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }
    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrdreCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.info("could not find restaurant with id {} ",restaurant.getId());
            throw new OrderDomainException("could not find restaurant with id "+restaurant.getId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomerExist(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.info("could not find customer with customer id {} ", customerId);
            throw new OrderDomainException("no customer with this id "+customerId );
        }
    }
    private void saveOrder(Order order)
    {
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            log.error("Failed to save order !!");
            throw new DomainException("Failed to save order ");
        }
        log.info("order is saved with id {} ", order.getId().getValue());
    }


}
