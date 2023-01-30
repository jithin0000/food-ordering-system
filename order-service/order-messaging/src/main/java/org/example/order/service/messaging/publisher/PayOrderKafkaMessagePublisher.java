package org.example.order.service.messaging.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import org.example.kafka.producer.KafkaMessageHelper;
import org.example.kafka.producer.service.KafkaProducer;
import org.example.order.domain.service.OrderServiceConfigData;
import org.example.order.domain.service.ports.output.message.publisher.restuarant.OrderPaidRestaurantRequestMessagePublisher;
import org.example.order.service.domain.events.OrderPaidEvent;
import org.example.order.service.messaging.mapper.OrderMessagingDataMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         OrderServiceConfigData orderServiceConfigData,
                                         KafkaMessageHelper kafkaMessageHelper,
                                         KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for order id: {} ", orderId);
        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    restaurantApprovalRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModelRequest"
                    )
            );
            log.info("Restaurant approval Request model sent to kafka for order id: {} ", restaurantApprovalRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("error while sending restaurant approval  request model");
            e.printStackTrace();
        }
    }


}
