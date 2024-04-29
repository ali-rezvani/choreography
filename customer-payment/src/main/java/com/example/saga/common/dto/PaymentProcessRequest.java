package com.example.saga.common.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PaymentProcessRequest(Integer customerId,
                                    UUID orderId,
                                    Integer amount) {

}
