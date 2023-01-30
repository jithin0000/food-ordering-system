package org.example.order.service.data.access.restaurant.entity;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class RestaurantEntityId implements Serializable {
    @Id
    private UUID restaurantId;
    @Id
    private UUID productId;

}
