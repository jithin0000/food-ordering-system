package org.example.order.service.messaging.publisher;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.kafka.order.avro.model.PaymentRequestAvroModel;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class OrderKafkaMessageHelper {
    public ListenableFutureCallback<SendResult<String, PaymentRequestAvroModel>> getKafkaCallBack(String paymentRequestTopicName, PaymentRequestAvroModel paymentRequestAvroModel) {
        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending payment request avro model message {} to topic {} ",
                        paymentRequestAvroModel.toString(), paymentRequestTopicName);
            }

            @Override
            public void onSuccess(SendResult<String, PaymentRequestAvroModel> result) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("Message received successful");
            }
        };
    }
}
