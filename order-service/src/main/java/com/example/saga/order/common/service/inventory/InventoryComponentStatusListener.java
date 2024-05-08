package com.example.saga.order.common.service.inventory;

import com.example.saga.order.common.dto.OrderInventoryDto;
import com.example.saga.order.common.service.OrderComponentStatusListener;

public interface InventoryComponentStatusListener extends OrderComponentStatusListener<OrderInventoryDto> {
}
