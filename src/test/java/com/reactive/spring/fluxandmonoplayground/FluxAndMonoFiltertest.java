package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFiltertest {
    List<String> names = Arrays.asList("adam","anna","jack","jenny");

    @Test
    public void filterTest(){
        Flux<String> stringFlux = Flux.fromIterable(names).filter(s->s.startsWith("a")).log();

        StepVerifier.create(stringFlux).expectNext("adam","anna").verifyComplete();
    }
}
