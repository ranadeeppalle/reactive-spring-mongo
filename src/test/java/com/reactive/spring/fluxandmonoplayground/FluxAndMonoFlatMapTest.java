package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoFlatMapTest {
    List<String> names = Arrays.asList("A","B","C","D","E","F");

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })//used when DB or external service is called fpr each and every element
            .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_usingParallel() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .window(2)// Passes flux of flux with size of 2 like Flux<Flux<String>> -> (A,B), (C,D), (E,F)
                .flatMap(s -> s.map(this::convertToList).subscribeOn(parallel()))//used when DB or external service is called fpr each and every element
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_usingParallel_maintain_order() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .window(2)// Passes flux of flux with size of 2 like Flux<Flux<String>> -> (A,B), (C,D), (E,F)
                .flatMapSequential(s -> s.map(this::convertToList).subscribeOn(parallel()))//used when DB or external service is called fpr each and every element
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }
}
