package com.example.saga.order.application.service;

import com.example.saga.order.application.mapper.EntityDtoMapper;
import com.example.saga.order.application.repository.PurchaseOrderRepository;
import com.example.saga.order.common.dto.OrderCreateRequest;
import com.example.saga.order.common.dto.OrderDetail;
import com.example.saga.order.common.dto.PurchaseOrderDto;
import com.example.saga.order.common.service.OrderEventListener;
import com.example.saga.order.common.service.OrderService;
import com.example.saga.order.common.service.inventory.InventoryComponentFetcher;
import com.example.saga.order.common.service.payment.PaymentComponentFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderEventListener orderEventListener;
    private final PaymentComponentFetcher paymentComponentFetcher;
    private final InventoryComponentFetcher inventoryComponentFetcher;

    @Override
    public Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request) {
        var entity = EntityDtoMapper.toPurchaseOrder(request);
        return this.purchaseOrderRepository.save(entity)
                .map(EntityDtoMapper::toPurchaseOrderDto)
                .doOnNext(orderEventListener::emitOrderCreated);
    }

    @Override
    public Flux<PurchaseOrderDto> getAll() {
        return this.purchaseOrderRepository.findAll()
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<OrderDetail> getOrderDetail(UUID orderId) {
        return this.purchaseOrderRepository.findById(orderId)
                .map(EntityDtoMapper::toPurchaseOrderDto)
                .flatMap(dto -> this.paymentComponentFetcher.getComponent(orderId)
                        .zipWith(this.inventoryComponentFetcher.getComponent(orderId))
                        .map(t -> EntityDtoMapper.toOrderDetails(dto, t.getT1(), t.getT2())));
    }
}
