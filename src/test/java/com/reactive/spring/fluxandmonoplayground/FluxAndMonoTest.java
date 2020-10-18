package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring");
        stringFlux.subscribe(System.out::println);
    }

    @Test
    public void fluxExceptionTest() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring")
               // .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .log();
        stringFlux.subscribe(System.out::println, (e) -> System.err.println(e), () -> System.out.println("completed"));
    }

    @Test
    public void fluxTest_WithoutError() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring").log();
        //StepVerifier.create(stringFlux).expectNext("spring").expectNext("reactive spring").expectNext("spring boot").verifyComplete();
        StepVerifier.create(stringFlux).expectNext("spring").expectNext("spring boot").expectNext("reactive spring");//.verifyComplete();
    }

    @Test
    public void fluxTest_WithoutError1() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring").log();
        //StepVerifier.create(stringFlux).expectNext("spring").expectNext("reactive spring").expectNext("spring boot").verifyComplete();
        StepVerifier.create(stringFlux).expectNext("spring","spring boot","reactive spring").verifyComplete();
    }

    @Test
    public void fluxTest_WithError() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .log();
        //StepVerifier.create(stringFlux).expectNext("spring").expectNext("reactive spring").expectNext("spring boot").verifyComplete();
        StepVerifier.create(stringFlux).expectNext("spring").expectNext("spring boot").expectNext("reactive spring")
                .expectError(RuntimeException.class)
                //.expectErrorMessage("Exception occured")
                .verify();
    }

    @Test
    public void fluxTest_ElemetsCount() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .log();
        //StepVerifier.create(stringFlux).expectNext("spring").expectNext("reactive spring").expectNext("spring boot").verifyComplete();
        StepVerifier.create(stringFlux).expectNextCount(3)
                .expectError(RuntimeException.class)
                //.expectErrorMessage("Exception occured")
                .verify();
    }

    @Test
    public void monoTest(){
        Mono<String> stringMono = Mono.just("spring").log();
        StepVerifier.create(stringMono).expectNext("spring").verifyComplete();
    }

    @Test
    public void monoTestError(){

        StepVerifier.create(Mono.error(new RuntimeException("Exception occured"))).expectError(RuntimeException.class).verify();
    }
}
