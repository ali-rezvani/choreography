package com.example.saga.order.common.service;

import com.example.saga.order.common.dto.PurchaseOrderDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderFulfillmentService {
    Mono<PurchaseOrderDto> complete(UUID orderId);
    Mono<PurchaseOrderDto> cancel(UUID orderId);
}
