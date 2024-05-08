package com.example.saga.order.application.service;

import com.example.saga.order.application.entity.OrderPayment;
import com.example.saga.order.application.mapper.EntityDtoMapper;
import com.example.saga.order.application.repository.OrderPaymentRepository;
import com.example.saga.order.common.dto.OrderPaymentDto;
import com.example.saga.order.common.service.payment.PaymentComponentFetcher;
import com.example.saga.order.common.service.payment.PaymentComponentStatusListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentComponentService implements PaymentComponentFetcher, PaymentComponentStatusListener {
    private static final OrderPaymentDto DEFAULT=OrderPaymentDto.builder().build();
    private final OrderPaymentRepository orderPaymentRepository;
    @Override
    public Mono<OrderPaymentDto> getComponent(UUID orderId) {
        return this.orderPaymentRepository.findByOrderId(orderId)
                .map(EntityDtoMapper::toOrderPaymentDto)
                .defaultIfEmpty(DEFAULT);
    }

    @Override
    public Mono<Void> onSuccess(OrderPaymentDto message) {
        return this.orderPaymentRepository.findByOrderId(message.orderId())
                .switchIfEmpty(Mono.defer(()->this.add(message,true)))
                .then();
    }

    @Override
    public Mono<Void> onFailure(OrderPaymentDto message) {
        return this.orderPaymentRepository.findByOrderId(message.orderId())
                .switchIfEmpty(Mono.defer(()->this.add(message,false)))
                .then();
    }

    @Override
    public Mono<Void> onRollback(OrderPaymentDto message) {
        return this.orderPaymentRepository.findByOrderId(message.orderId())
                .doOnNext(o->o.setStatus(message.status()))
                .flatMap(this.orderPaymentRepository::save)
                .then();
    }

    private Mono<OrderPayment> add(OrderPaymentDto dto,boolean isSuccess){
        var entity=EntityDtoMapper.toOrderPayment(dto);
        entity.setSuccess(isSuccess);
        return this.orderPaymentRepository.save(entity);
    }
}
