package org.example.order.domain.service.ports.input.messageListener.restaurantapproval;

import org.example.order.domain.service.dto.message.RestaurantApprovalResponse;

public interface RestaurantResponseMessageListener {
    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
