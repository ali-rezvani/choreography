package com.example.saga.order.application.service;

import com.example.saga.common.events.order.OrderStatus;
import com.example.saga.order.application.repository.PurchaseOrderRepository;
import com.example.saga.order.common.dto.OrderShipmentSchedule;
import com.example.saga.order.common.service.shipping.ShippingComponentStatusListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingComponentService implements ShippingComponentStatusListener {

    private final PurchaseOrderRepository purchaseOrderRepository;
    @Override
    public Mono<Void> onSuccess(OrderShipmentSchedule message) {
        return this.purchaseOrderRepository
                .findByOrderIdAndStatus(message.orderId(), OrderStatus.COMPLETED)
                .doOnNext(o->o.setDeliveryDate(message.deliveryDate()))
                .flatMap(this.purchaseOrderRepository::save)
                .then();
    }

    @Override
    public Mono<Void> onFailure(OrderShipmentSchedule message) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> onRollback(OrderShipmentSchedule message) {
        return Mono.empty();
    }
}
