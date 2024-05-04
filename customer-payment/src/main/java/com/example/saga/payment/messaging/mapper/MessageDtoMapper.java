package com.example.saga.payment.messaging.mapper;

import com.example.saga.payment.common.dto.PaymentDto;
import com.example.saga.payment.common.dto.PaymentProcessRequest;
import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.payment.PaymentEvent;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

public class MessageDtoMapper {

    public static PaymentProcessRequest toPaymentProcessRequest(OrderEvent.OrderCreated event) {
        return PaymentProcessRequest
                .builder()
                .customerId(event.customerId())
                .orderId(event.orderId())
                .amount(event.totalAmount())
                .build();
    }

    public static PaymentEvent toPaymentDeductedEvent(PaymentDto dto) {
        return PaymentEvent.PaymentDeducted
                .builder()
                .paymentId(dto.paymentId())
                .customerId(dto.customerId())
                .orderId(dto.orderId())
                .amount(dto.amount())
                .createdAt(Instant.now())
                .build();
    }

    public static PaymentEvent toPaymentRefundedEvent(PaymentDto dto) {
        return PaymentEvent.PaymentRefunded
                .builder()
                .paymentId(dto.paymentId())
                .customerId(dto.customerId())
                .orderId(dto.orderId())
                .amount(dto.amount())
                .createdAt(Instant.now())
                .build();
    }
    public static Function<Throwable, Mono<PaymentEvent>> toPaymentDeclinedEvent(OrderEvent.OrderCreated event) {
        return ex -> Mono
                .fromSupplier(() -> PaymentEvent.PaymentDeclined
                        .builder()
                        .orderId(event.orderId())
                        .amount(event.totalAmount())
                        .customerId(event.customerId())
                        .message(ex.getMessage())
                        .createdAt(Instant.now())
                        .build());
    }


}
