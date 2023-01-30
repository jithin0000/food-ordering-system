package org.example.order.domain.service.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.domain.valueobject.OrderStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class CreateOrderResponse {
    @NotNull
    private final UUID orderTrackingId;
    @NotNull
    private final OrderStatus orderStatus;
    @NotNull
    private final String message;
}
