package com.example.saga.order.common.service;

import com.example.saga.order.common.dto.PurchaseOrderDto;

public interface OrderEventListener {
    void emitOrderCreated(PurchaseOrderDto dto);
}
