package com.example.saga.shipping.application.service;

import com.example.saga.common.events.shipping.ShippingStatus;
import com.example.saga.common.util.DuplicateEventValidator;
import com.example.saga.shipping.application.entity.Shipment;
import com.example.saga.shipping.application.mapper.EntityDtoMapper;
import com.example.saga.shipping.application.repository.ShipmentRepository;
import com.example.saga.shipping.common.dto.ScheduleRequest;
import com.example.saga.shipping.common.dto.ShipmentDto;
import com.example.saga.shipping.common.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {
    private final ShipmentRepository shipmentRepository;
    @Override
    public Mono<Void> addShipment(ScheduleRequest request) {
        return DuplicateEventValidator.validate(
                this.shipmentRepository.existsByOrderId(request.orderId()),
                Mono.defer(()->this.add(request))
        );
    }

    @Override
    public Mono<Void> cancelShipment(UUID orderId) {
        return this.shipmentRepository.deleteByOrderId(orderId);
    }

    @Override
    public Mono<ShipmentDto> scheduleShipment(UUID orderId) {
        return this.shipmentRepository.findByOrderIdAndStatus(orderId,ShippingStatus.PENDING)
                .flatMap(this::schedule)
                .doOnNext(dto->log.info("shipping scheduled by orderId:{}",dto.orderId()));
    }

    private Mono<Void> add(ScheduleRequest request){
        var shipment= EntityDtoMapper.toShipment(request);
        shipment.setDeliveryDate(Instant.now());
        shipment.setStatus(ShippingStatus.PENDING);
        return this.shipmentRepository.save(shipment)
                .then();
    }

    private Mono<ShipmentDto> schedule(Shipment shipment){
//        shipment.setDeliveryDate(Instant.now().plus(Duration.ofDays(ThreadLocalRandom.current().nextInt())));
        shipment.setDeliveryDate(Instant.now().plus(Duration.ofDays(3)));
        shipment.setStatus(ShippingStatus.SCHEDULED);
        return this.shipmentRepository.save(shipment)
                .map(EntityDtoMapper::toShipmentDto);

    }
}
