package org.example.order.service.messaging.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.order.avro.model.PaymentRequestAvroModel;
import org.example.kafka.producer.KafkaMessageHelper;
import org.example.kafka.producer.service.KafkaProducer;
import org.example.order.domain.service.OrderServiceConfigData;
import org.example.order.domain.service.ports.output.message.publisher.payment.OrderCancelPaymentRequestMessagePublisher;
import org.example.order.service.domain.events.OrderCancellEvent;
import org.example.order.service.messaging.mapper.OrderMessagingDataMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CancelOrderKafkaMessagePublisher implements OrderCancelPaymentRequestMessagePublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    public CancelOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                            OrderServiceConfigData orderServiceConfigData,
                                            KafkaMessageHelper kafkaMessageHelper, KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(OrderCancellEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for order id: {} ", orderId);
        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper.orderCancelledEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModel"
                    )
            );
            log.info("Payment Request model sent to kafka for order id: {} ", paymentRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("error while sending payment request model");
            e.printStackTrace();
        }
    }


}
