package com.example.saga.payment.application.mapper;

import com.example.saga.payment.application.entity.CustomerPayment;
import com.example.saga.payment.common.dto.PaymentDto;
import com.example.saga.payment.common.dto.PaymentProcessRequest;

public class EntityDtoMapper {

    public static CustomerPayment toCustomerPayment(PaymentProcessRequest request){
        return CustomerPayment.builder()
                .customerId(request.customerId())
                .orderId(request.orderId())
                .amount(request.amount())
                .build();
    }

    public static PaymentDto toPaymentDto(CustomerPayment customerPayment){
        return PaymentDto.builder()
                .paymentId(customerPayment.getPaymentId())
                .orderId(customerPayment.getOrderId())
                .customerId(customerPayment.getCustomerId())
                .amount(customerPayment.getAmount())
                .status(customerPayment.getStatus())
                .status(customerPayment.getStatus())
                .build();
    }
}
