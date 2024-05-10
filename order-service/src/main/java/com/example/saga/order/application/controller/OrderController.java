package com.example.saga.order.application.controller;

import com.example.saga.order.common.dto.OrderCreateRequest;
import com.example.saga.order.common.dto.OrderDetail;
import com.example.saga.order.common.dto.PurchaseOrderDto;
import com.example.saga.order.common.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/rest/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    Mono<ResponseEntity<PurchaseOrderDto>> placeOrder(@RequestBody Mono<OrderCreateRequest> mono){
        return mono.flatMap(this.orderService::placeOrder)
                .map(ResponseEntity.accepted()::body);
    }
    @GetMapping("all")
    public Flux<PurchaseOrderDto> getAllOrders(){
        return this.orderService.getAll();
    }
    @GetMapping("{orderId}")
    public Mono<OrderDetail> getOrderDetails(@PathVariable UUID orderId){
        return this.orderService.getOrderDetail(orderId);
    }
}
