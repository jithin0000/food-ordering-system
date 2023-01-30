package org.example.order.service.messaging.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.consumer.KafkaConsumer;
import org.example.kafka.order.avro.model.PaymentResponseAvroModel;
import org.example.kafka.order.avro.model.PaymentStatus;
import org.example.order.domain.service.ports.input.messageListener.payment.PaymentResponseMessageListener;
import org.example.order.service.messaging.mapper.OrderMessagingDataMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public PaymentResponseKafkaListener(PaymentResponseMessageListener paymentResponseMessageListener, OrderMessagingDataMapper orderMessagingDataMapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.KEY) List<String> keys,
                        @Header(KafkaHeaders.PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment response received with keys: {}, partitions: {} and offsets: {}",
                keys.size(), keys, partitions.toString(), offsets.toString()
        );
        messages.forEach(paymentResponseAvroModel -> {
            if (paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.COMPLETED) {
                log.info("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCompleted(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel)

                );
            } else if (paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.CANCELLED || paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.FAILED) {
                log.info("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCancelled(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));

            }
        });

    }
}
