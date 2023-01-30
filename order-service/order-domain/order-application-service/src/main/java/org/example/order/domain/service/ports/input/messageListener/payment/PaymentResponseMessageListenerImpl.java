package org.example.order.domain.service.ports.input.messageListener.payment;

import lombok.extern.slf4j.Slf4j;
import org.example.order.domain.service.dto.message.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
@Slf4j
@Service
@Validated
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {
    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

    }
}
