package org.example.order.domain.service.ports.input.messageListener.payment;

import org.example.order.domain.service.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse);
    void paymentCancelled(PaymentResponse paymentResponse);
}
