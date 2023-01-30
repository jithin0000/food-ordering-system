package org.example.order.service.messaging.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.consumer.KafkaConsumer;
import org.example.kafka.order.avro.model.OrderApprovalStatus;
import org.example.kafka.order.avro.model.PaymentResponseAvroModel;
import org.example.kafka.order.avro.model.PaymentStatus;
import org.example.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import org.example.order.domain.service.ports.input.messageListener.restaurantapproval.RestaurantResponseMessageListener;
import org.example.order.service.messaging.mapper.OrderMessagingDataMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalRequesetKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantResponseMessageListener restaurantResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public RestaurantApprovalRequesetKafkaListener(RestaurantResponseMessageListener restaurantResponseMessageListener, OrderMessagingDataMapper orderMessagingDataMapper) {
        this.restaurantResponseMessageListener = restaurantResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.KEY) List<String> keys,
                        @Header(KafkaHeaders.PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of restaurant approval response received with keys: {}, partitions: {} and offsets: {}",
                keys.size(), keys, partitions.toString(), offsets.toString()
        );
        messages.forEach(restaurantApprovalResponseAvroModel -> {
            if (restaurantApprovalResponseAvroModel.getOrderApprovalStatus() == OrderApprovalStatus.APPROVED) {
                log.info("Processing order approved for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                restaurantResponseMessageListener.orderApproved(
                        orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel)

                );
            } else if (restaurantApprovalResponseAvroModel.getOrderApprovalStatus() == OrderApprovalStatus.REJECTED ) {
                log.info("Processing unsuccessful payment for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                restaurantResponseMessageListener.orderRejected(
                        orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel));

            }
        });

    }
}
