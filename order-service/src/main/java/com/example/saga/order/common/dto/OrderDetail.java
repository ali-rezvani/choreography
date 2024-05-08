package com.example.saga.order.common.dto;

import lombok.Builder;

@Builder
public record OrderDetail(PurchaseOrderDto order,
                          OrderPaymentDto payment,
                          OrderInventoryDto inventory) {
}
