package com.example.saga.order.common.service;

import com.example.saga.order.common.dto.OrderCreateRequest;
import com.example.saga.order.common.dto.OrderDetail;
import com.example.saga.order.common.dto.PurchaseOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {
    Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request);
    Flux<PurchaseOrderDto> getAll();
    Mono<OrderDetail> getOrderDetail(UUID orderId);
}
