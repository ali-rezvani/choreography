package com.example.saga.order.common.dto;

public record OrderCreateRequest(Integer customerId,
                                 Integer productId,
                                 Integer quantity,
                                 Integer unitPrice) {
}
