package com.example.saga.common.util;

import com.example.saga.common.exception.EventAlreadyProcessedException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
public class DuplicateEventValidator {

    public static Function<Mono<Boolean>,Mono<Void>> emitErrorForRedundantProcessing(){
        return mono->mono
                .flatMap(b-> b?Mono.error(new EventAlreadyProcessedException()):Mono.empty())
                .then()
                .doOnError(EventAlreadyProcessedException.class,ex->log.warn("Duplicate event"))
                .then();
    }

    public static <T> Mono<T> validate(Mono<Boolean> eventValidationPublisher,Mono<T> eventProcessingPublisher){
        return eventValidationPublisher
                .transform(emitErrorForRedundantProcessing())
                .then(eventProcessingPublisher);

    }
}
