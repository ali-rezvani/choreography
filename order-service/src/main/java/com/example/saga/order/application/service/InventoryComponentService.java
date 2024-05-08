package com.example.saga.order.application.service;

import com.example.saga.order.application.entity.OrderInventory;
import com.example.saga.order.application.mapper.EntityDtoMapper;
import com.example.saga.order.application.repository.OrderInventoryRepository;
import com.example.saga.order.common.dto.OrderInventoryDto;
import com.example.saga.order.common.service.inventory.InventoryComponentFetcher;
import com.example.saga.order.common.service.inventory.InventoryComponentStatusListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryComponentService implements InventoryComponentFetcher, InventoryComponentStatusListener {
    private static final OrderInventoryDto DEFAULT = OrderInventoryDto.builder().build();
    private final OrderInventoryRepository orderInventoryRepository;

    @Override
    public Mono<OrderInventoryDto> getComponent(UUID orderId) {
        return this.orderInventoryRepository.findByOrderId(orderId)
                .map(EntityDtoMapper::toOrderInventoryDto)
                .defaultIfEmpty(DEFAULT);
    }

    @Override
    public Mono<Void> onSuccess(OrderInventoryDto message) {
        return this.orderInventoryRepository.findByOrderId(message.orderId())
                .switchIfEmpty(this.add(message,true))
                .then();
    }

    @Override
    public Mono<Void> onFailure(OrderInventoryDto message) {
        return this.orderInventoryRepository.findByOrderId(message.orderId())
                .switchIfEmpty(this.add(message,false))
                .then();
    }

    @Override
    public Mono<Void> onRollback(OrderInventoryDto message) {
        return this.orderInventoryRepository.findByOrderId(message.orderId())
                .doOnNext(o->o.setStatus(message.status()))
                .flatMap(this.orderInventoryRepository::save)
                .then();
    }

    private Mono<OrderInventory> add(OrderInventoryDto dto,boolean isSuccess){
        var entity=EntityDtoMapper.toOrderInventory(dto);
        entity.setSuccess(isSuccess);
        return this.orderInventoryRepository.save(entity);
    }
}
