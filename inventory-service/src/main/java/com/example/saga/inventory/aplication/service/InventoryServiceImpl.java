package com.example.saga.inventory.aplication.service;

import com.example.saga.common.events.inventory.InventoryStatus;
import com.example.saga.common.util.DuplicateEventValidator;
import com.example.saga.inventory.aplication.entity.OrderInventory;
import com.example.saga.inventory.aplication.entity.Product;
import com.example.saga.inventory.aplication.mapper.EntityDtoMapper;
import com.example.saga.inventory.aplication.repository.OrderInventoryRepository;
import com.example.saga.inventory.aplication.repository.ProductRepository;
import com.example.saga.inventory.common.dto.InventoryDeductRequest;
import com.example.saga.inventory.common.dto.OrderInventoryDto;
import com.example.saga.inventory.common.exception.OutOfStockException;
import com.example.saga.inventory.common.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final Mono<Product> OUT_OF_STOCK=Mono.error(OutOfStockException::new);
    private final ProductRepository productRepository;
    private final OrderInventoryRepository orderInventoryRepository;
    @Override
    @Transactional
    public Mono<OrderInventoryDto> deduct(InventoryDeductRequest request) {
        return DuplicateEventValidator.validate(
                this.orderInventoryRepository.existsByOrderId(request.orderId()),
                this.productRepository.findById(request.productId()))
                .filter(p->p.getAvailableQuantity()>= request.quantity())
                .switchIfEmpty(OUT_OF_STOCK)
                .flatMap(p->this.deductInventory(p,request))
                .doOnNext(dto->log.info("inventory deducted for {}",dto.orderId()));
    }

    @Override
    @Transactional
    public Mono<OrderInventoryDto> restore(UUID orderId) {
        return this.orderInventoryRepository.findByOrderIdAndStatus(orderId,InventoryStatus.DEDUCTED)
                .zipWhen(i->this.productRepository.findById(i.getProductId()))
                .flatMap(t->this.restore(t.getT1(),t.getT2()))
                .doOnNext(dto->log.info("restored quantity {} for {}",dto.quantity(),dto.orderId()));
    }

    private Mono<OrderInventoryDto> deductInventory(Product product,InventoryDeductRequest request){
        var orderInventory= EntityDtoMapper.toOrderInventory(request);
        product.setAvailableQuantity(product.getAvailableQuantity()-request.quantity());
        orderInventory.setStatus(InventoryStatus.DEDUCTED);
        return this.productRepository.save(product)
                .then(this.orderInventoryRepository.save(orderInventory))
                .map(EntityDtoMapper::toOrderInventoryDto);
    }

    private Mono<OrderInventoryDto> restore(OrderInventory orderInventory,Product product){
        product.setAvailableQuantity(product.getAvailableQuantity()+orderInventory.getQuantity());
        orderInventory.setStatus(InventoryStatus.RESTORED);
        return this.productRepository.save(product)
                .then(this.orderInventoryRepository.save(orderInventory))
                .map(EntityDtoMapper::toOrderInventoryDto);
    }
}
