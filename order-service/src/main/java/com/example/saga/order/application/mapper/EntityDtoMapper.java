package com.example.saga.order.application.mapper;

import com.example.saga.common.events.order.OrderStatus;
import com.example.saga.order.application.entity.OrderInventory;
import com.example.saga.order.application.entity.OrderPayment;
import com.example.saga.order.application.entity.PurchaseOrder;
import com.example.saga.order.common.dto.*;

public class EntityDtoMapper {
    public static PurchaseOrder toPurchaseOrder(OrderCreateRequest request){
        return PurchaseOrder.builder()
                .customerId(request.customerId())
                .quantity(request.quantity())
                .productId(request.productId())
                .unitPrice(request.unitPrice())
                .amount(request.unitPrice()* request.quantity())
                .status(OrderStatus.PENDING)
                .build();
    }

    public static PurchaseOrderDto toPurchaseOrderDto(PurchaseOrder entity){
        return PurchaseOrderDto.builder()
                .orderId(entity.getOrderId())
                .customerId(entity.getCustomerId())
                .productId(entity.getProductId())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity())
                .status(entity.getStatus())
                .deliveryDate(entity.getDeliveryDate())
                .amount(entity.getAmount())
                .build();
    }

    public static OrderPayment toOrderPayment(OrderPaymentDto dto){
        return OrderPayment.builder()
                .orderId(dto.orderId())
                .paymentId(dto.paymentId())
                .message(dto.message())
                .status(dto.status())
                .build();
    }
    public static OrderPaymentDto toOrderPaymentDto(OrderPayment entity) {
        return OrderPaymentDto.builder()
                .status(entity.getStatus())
                .paymentId(entity.getPaymentId())
                .orderId(entity.getOrderId())
                .message(entity.getMessage())
                .build();
    }

    public static OrderInventory toOrderInventory(OrderInventoryDto dto) {
        return OrderInventory.builder()
                .inventoryId(dto.inventoryId())
                .orderId(dto.orderId())
                .status(dto.status())
                .message(dto.message())
                .build();
    }

    public static OrderInventoryDto toOrderInventoryDto(OrderInventory entity) {
        return OrderInventoryDto.builder()
                .inventoryId(entity.getInventoryId())
                .orderId(entity.getOrderId())
                .status(entity.getStatus())
                .message(entity.getMessage())
                .build();
    }

    public static OrderDetail toOrderDetails(PurchaseOrderDto orderDto,
                                             OrderPaymentDto paymentDto,
                                             OrderInventoryDto inventoryDto) {
        return OrderDetail.builder()
                .order(orderDto)
                .payment(paymentDto)
                .inventory(inventoryDto)
                .build();
    }
}
