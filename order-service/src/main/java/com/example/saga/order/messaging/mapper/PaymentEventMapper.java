package com.example.saga.order.messaging.mapper;

import com.example.saga.common.events.payment.PaymentEvent;
import com.example.saga.common.events.payment.PaymentStatus;
import com.example.saga.order.common.dto.OrderPaymentDto;

public class PaymentEventMapper {
    public static OrderPaymentDto toDto(PaymentEvent.PaymentDeducted event){
        return OrderPaymentDto.builder()
                .orderId(event.orderId())
                .paymentId(event.paymentId())
                .status(PaymentStatus.DEDUCTED)
                .build();
    }

    public static OrderPaymentDto toDto(PaymentEvent.PaymentRefunded event){
        return OrderPaymentDto.builder()
                .orderId(event.orderId())
                .paymentId(event.paymentId())
                .status(PaymentStatus.REFUNDED)
                .build();
    }
    public static OrderPaymentDto toDto(PaymentEvent.PaymentDeclined event){
        return OrderPaymentDto.builder()
                .orderId(event.orderId())
                .status(PaymentStatus.DECLINED)
                .message(event.message())
                .build();
    }
}
