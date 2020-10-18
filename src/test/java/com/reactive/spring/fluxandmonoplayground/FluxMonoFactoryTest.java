package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxMonoFactoryTest {
    List<String> names = Arrays.asList("adam","anna","jack","jenny");

    @Test
    public void fluxUsingIterable(){
        Flux<String> namesFlux = Flux.fromIterable(names);

        StepVerifier.create(namesFlux).expectNext("adam","anna","jack","jenny").verifyComplete();
    }


    @Test
    public void fluxUsingRange(){
        Flux<Integer> rangeFlux = Flux.range(1,5);

        StepVerifier.create(rangeFlux).expectNext(1,2,3,4,5).verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<Object> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log()).verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){
        Supplier<String> stringSupplier = () -> "adam";

        Mono<Object> mono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(mono.log()).expectNext("adam").verifyComplete();
    }
}
