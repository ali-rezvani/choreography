package com.example.saga.application.mapper;

import com.example.saga.application.entity.CustomerPayment;
import com.example.saga.common.dto.PaymentDto;
import com.example.saga.common.dto.PaymentProcessRequest;

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
