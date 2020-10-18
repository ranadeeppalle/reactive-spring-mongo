package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> merge = Flux.merge(flux1, flux2).log();
        StepVerifier.create(merge)
                .expectSubscription()
                .expectNextCount(6)
                //.expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> concat = Flux.concat(flux1, flux2).log();
        StepVerifier.create(concat)
                .expectSubscription()
               // .expectNextCount(6)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withVirtualDelay(){
        VirtualTimeScheduler.getOrSet();
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> concat = Flux.concat(flux1, flux2).log();
        StepVerifier.withVirtualTime(()  ->concat.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                // .expectNextCount(6)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> concat = Flux.zip(flux1, flux2, (t1,t2) -> t1.concat(t2)).log();
        StepVerifier.create(concat)
                .expectSubscription()
                // .expectNextCount(6)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

}
