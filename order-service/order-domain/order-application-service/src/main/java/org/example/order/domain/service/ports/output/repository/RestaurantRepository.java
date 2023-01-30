package org.example.order.domain.service.ports.output.repository;

import org.example.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurantId);

}
