package com.example.saga.order.application.repository;

import com.example.saga.common.events.order.OrderStatus;
import com.example.saga.order.application.entity.PurchaseOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends ReactiveCrudRepository<PurchaseOrder,UUID> {
    Mono<PurchaseOrder> findByOrderIdAndStatus(UUID orderId, OrderStatus status);

    @Query("""
                SELECT po.*
                FROM   purchase_order po,
                       order_payment op,
                       order_inventory oi
                WHERE  op.order_id = po.order_id
                       AND oi.order_id = po.order_id
                       AND op.success
                       AND oi.success
                       AND po.status = :status
                       AND po.order_id = :orderId
            """)
    Mono<PurchaseOrder> getWhenOrderComponentsCompleted(UUID orderId,OrderStatus status);
}
