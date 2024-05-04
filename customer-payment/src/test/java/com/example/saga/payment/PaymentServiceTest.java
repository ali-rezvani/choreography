package com.example.saga.payment;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.payment.PaymentEvent;
import com.example.saga.payment.application.repository.CustomerRepository;
//import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

@TestPropertySource(properties = {
        "spring.cloud.function.definition=processor;orderEventProducer;paymentEventConsumer",
        "spring.cloud.stream.bindings.orderEventProducer-out-0.destination=order-events-topic",
        "spring.cloud.stream.bindings.paymentEventConsumer-in-0.destination=payment-events-topic"
})
public class PaymentServiceTest extends AbstractIntegrationTest {
    private static final Sinks.Many<OrderEvent> reqSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Sinks.Many<PaymentEvent> resSink = Sinks.many().unicast().onBackpressureBuffer();

    @Autowired
    private CustomerRepository customerRepository;
    @Test
    public void processPaymentTest() {
        var orderCreatedEvent=TestDataUtil.createOrderCreatedEvent(1,1,2,3);
        resSink.asFlux()
                .doFirst(()->reqSink.tryEmitNext(orderCreatedEvent))
                .next()
                .timeout(Duration.ofSeconds(1))
                .cast(PaymentEvent.PaymentDeducted.class)
                .as(StepVerifier::create)
                .consumeNextWith(e->{
                    Assertions.assertNotNull(e.paymentId());
                    Assertions.assertEquals(orderCreatedEvent.orderId(),e.orderId());
                    Assertions.assertEquals(6,e.amount());
                })
                .verifyComplete();
        this.customerRepository.findById(1)
                .as(StepVerifier::create)
                .consumeNextWith(c-> Assertions.assertEquals(94,c.getBalance()))
                .verifyComplete();
    }

    @TestConfiguration
    public class TestConfig{
        @Bean
        public Supplier<Flux<OrderEvent>> orderEventProducer(){
            return reqSink::asFlux;
        }

        @Bean
        public Consumer<Flux<PaymentEvent>> paymentEventConsumer(){
            return flux->flux.doOnNext(resSink::tryEmitNext).subscribe();
        }
    }
}
