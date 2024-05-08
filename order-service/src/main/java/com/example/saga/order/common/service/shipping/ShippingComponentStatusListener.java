package com.example.saga.order.common.service.shipping;

import com.example.saga.order.common.dto.OrderPaymentDto;
import com.example.saga.order.common.dto.OrderShipmentSchedule;
import com.example.saga.order.common.service.OrderComponentStatusListener;

public interface ShippingComponentStatusListener extends OrderComponentStatusListener<OrderShipmentSchedule> {

}
