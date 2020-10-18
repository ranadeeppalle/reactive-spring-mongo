package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorhandling(){
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    System.out.println("exception is "+e);
                    return  Flux.just("default");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                //.expectError(RuntimeException.class).verify();
        .verifyComplete();
    }

    @Test
    public void fluxErrorhandling_onErrorReturn(){
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                //.expectError(RuntimeException.class).verify();
                .verifyComplete();
    }

    @Test
    public void fluxErrorhandling_onErrorMap(){
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap( e-> new CustomException(e));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")

               .expectError(CustomException.class).verify();

    }

    @Test
    public void fluxErrorhandling_onErrorMap_with_retry(){
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap( e-> new CustomException(e))
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class).verify();

    }

    @Test
    public void fluxErrorhandling_onErrorMap_with_retry_backoff(){
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap( e-> new CustomException(e))
                .retryWhen(Retry.backoff(2,Duration.ofSeconds(5)));// retryBackoff(2, Duration.ofSeconds(5));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class).verify();

    }
}
