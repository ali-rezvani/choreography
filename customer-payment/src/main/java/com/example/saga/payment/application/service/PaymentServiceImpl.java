package com.example.saga.payment.application.service;

import com.example.saga.payment.application.entity.Customer;
import com.example.saga.payment.application.entity.CustomerPayment;
import com.example.saga.payment.application.mapper.EntityDtoMapper;
import com.example.saga.payment.application.repository.CustomerRepository;
import com.example.saga.payment.application.repository.PaymentRepository;
import com.example.saga.payment.common.dto.PaymentDto;
import com.example.saga.payment.common.dto.PaymentProcessRequest;
import com.example.saga.common.events.payment.PaymentStatus;
import com.example.saga.payment.common.exception.CustomerNotFoundException;
import com.example.saga.payment.common.exception.InsufficientBalanceException;
import com.example.saga.payment.common.service.PaymentService;
import com.example.saga.common.util.DuplicateEventValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final Mono<Customer> CUSTOMER_NOT_FOUND = Mono.error(new CustomerNotFoundException());
    private final Mono<Customer> INSUFFICIENT_BALANCE = Mono.error(new InsufficientBalanceException());
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Mono<PaymentDto> process(PaymentProcessRequest request) {
        return DuplicateEventValidator.validate(
                        this.paymentRepository.existsByOrderId(request.orderId()),
                        this.customerRepository.findById(request.customerId()))
                .switchIfEmpty(CUSTOMER_NOT_FOUND)
                .filter(c -> c.getBalance() >= request.amount())
                .switchIfEmpty(INSUFFICIENT_BALANCE)
                .flatMap(c -> this.deductPayment(c, request))
                .doOnNext(dto -> log.info("Payment process successful for {}", dto.orderId()));
    }

    private Mono<PaymentDto> deductPayment(Customer customer, PaymentProcessRequest request) {
        var customerPayment = EntityDtoMapper.toCustomerPayment(request);
        customer.setBalance(customer.getBalance() - request.amount());
        customerPayment.setStatus(PaymentStatus.DEDUCTED);
        return this.customerRepository.save(customer)
                .then(this.paymentRepository.save(customerPayment))
                .map(EntityDtoMapper::toPaymentDto);
    }

    @Override
    @Transactional
    public Mono<PaymentDto> refund(UUID orderId) {
        return this.paymentRepository.findCustomerPaymentByOrOrderIdAndStatus(orderId, PaymentStatus.DEDUCTED)
                .zipWhen(cp -> this.customerRepository.findById(cp.getCustomerId()))
                .flatMap(tuple->this.refundPayment(tuple.getT1(),tuple.getT2()))
                .doOnNext(dto->log.info("Refund amount {} for {}",dto.amount(),dto.orderId()));
    }

    private Mono<PaymentDto> refundPayment(CustomerPayment payment, Customer customer) {
        customer.setBalance(customer.getBalance() + payment.getAmount());
        payment.setStatus(PaymentStatus.REFUNDED);
        return this.customerRepository.save(customer)
                .then(this.paymentRepository.save(payment))
                .map(EntityDtoMapper::toPaymentDto);
    }


}
