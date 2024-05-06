package com.example.saga.shipping.common.dto;

import com.example.saga.common.events.shipping.ShippingStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ShipmentDto(UUID shipmentId,
                          UUID orderId,
                          Integer productId,
                          Integer customerId,
                          Integer quantity,
                          Instant expectedDate,
                          ShippingStatus status) {
}
