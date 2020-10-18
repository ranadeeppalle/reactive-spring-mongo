package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTest {
    @Test
    public void backPressureTest(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        StepVerifier.create(finiteFlux).expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();
        finiteFlux.subscribe(element -> System.out.println("element is " + element)
                , e -> System.out.println("exception is "+e)
                , () -> System.out.println("completed")
                ,subscription -> subscription.request(2));
    }

    @Test
    public void backPressure_cancel(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();
        finiteFlux.subscribe(element -> System.out.println("element is " + element)
                , e -> System.out.println("exception is "+e)
                , () -> System.out.println("completed")
                ,subscription -> subscription.cancel());
    }

    @Test
    public void backPressure_customized(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
                                 @Override
                                 protected void hookOnNext(Integer value) {
                                     request(1);
                                     System.out.println("value received id "+value);
                                     if(value == 4) cancel();
                                 }
                             }
               );
    }
}
