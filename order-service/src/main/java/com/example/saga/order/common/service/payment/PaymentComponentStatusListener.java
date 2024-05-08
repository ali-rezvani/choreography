package com.example.saga.order.common.service.payment;

import com.example.saga.order.common.dto.OrderPaymentDto;
import com.example.saga.order.common.service.OrderComponentStatusListener;

public interface PaymentComponentStatusListener extends OrderComponentStatusListener<OrderPaymentDto> {

}
