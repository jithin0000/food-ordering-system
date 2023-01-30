package org.example.order.domain.service.ports.input.messageListener.restaurantapproval;

import lombok.extern.slf4j.Slf4j;
import org.example.order.domain.service.dto.message.RestaurantApprovalResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantResponseMessageListenerImpl implements RestaurantResponseMessageListener {
    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
