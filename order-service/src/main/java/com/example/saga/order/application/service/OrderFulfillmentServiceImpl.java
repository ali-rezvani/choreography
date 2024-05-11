package com.example.saga.order.application.service;

import com.example.saga.common.events.order.OrderStatus;
import com.example.saga.order.application.entity.PurchaseOrder;
import com.example.saga.order.application.mapper.EntityDtoMapper;
import com.example.saga.order.application.repository.PurchaseOrderRepository;
import com.example.saga.order.common.dto.PurchaseOrderDto;
import com.example.saga.order.common.service.OrderFulfillmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public Mono<PurchaseOrderDto> complete(UUID orderId) {
        return this.purchaseOrderRepository
                .getWhenOrderComponentsCompleted(orderId, OrderStatus.PENDING)
                .transform(this.updateStatus(OrderStatus.COMPLETED));
    }

    @Override
    public Mono<PurchaseOrderDto> cancel(UUID orderId) {
        return this.purchaseOrderRepository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                .transform(this.updateStatus(OrderStatus.CANCELLED));
    }

    private Function<Mono<PurchaseOrder>, Mono<PurchaseOrderDto>> updateStatus(OrderStatus status) {
        return mono -> mono
                .doOnSuccess(e -> e.setStatus(status))
                .flatMap(this.purchaseOrderRepository::save)
                .retryWhen(Retry.max(1).filter(OptimisticLockingFailureException.class::isInstance))
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }
}
