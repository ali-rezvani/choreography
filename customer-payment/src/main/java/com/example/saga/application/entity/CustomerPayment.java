package com.example.saga.application.entity;

import com.example.saga.common.events.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPayment {

    @Id
    private UUID paymentId;
    private UUID orderId;
    private Integer customerId;
    private PaymentStatus status;
    private Integer amount;
}
