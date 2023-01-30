package org.example.order.service.data.access.restaurant.adapter;

import org.example.order.domain.service.ports.output.repository.RestaurantRepository;
import org.example.order.service.data.access.restaurant.entity.RestaurantEntity;
import org.example.order.service.data.access.restaurant.mapper.RestaurantDataAccessMapper;
import org.example.order.service.data.access.restaurant.repository.RestaurantJpaRepository;
import org.example.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;


    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository, RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProductIds =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);

        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProductIds);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);

    }
}
