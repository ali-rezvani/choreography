package com.example.saga.order.application.service;

import com.example.saga.common.events.order.OrderStatus;
import com.example.saga.order.application.mapper.EntityDtoMapper;
import com.example.saga.order.application.repository.PurchaseOrderRepository;
import com.example.saga.order.common.dto.PurchaseOrderDto;
import com.example.saga.order.common.service.OrderFulfillmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    @Override
    public Mono<PurchaseOrderDto> complete(UUID orderId) {
        return this.purchaseOrderRepository
                .getWhenOrderComponentsCompleted(orderId,OrderStatus.PENDING)
                .doOnNext(o->o.setStatus(OrderStatus.COMPLETED))
                .flatMap(this.purchaseOrderRepository::save)
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<PurchaseOrderDto> cancel(UUID orderId) {
        return this.purchaseOrderRepository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                .doOnNext(o->o.setStatus(OrderStatus.CANCELLED))
                .flatMap(this.purchaseOrderRepository::save)
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }
}
