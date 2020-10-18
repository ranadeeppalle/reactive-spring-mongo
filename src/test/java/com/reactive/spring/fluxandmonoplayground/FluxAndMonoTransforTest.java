package com.reactive.spring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransforTest {
    List<String> names = Arrays.asList("adam","anna","jack","jenny");
    @Test
    public void transformUsingMap(){
        Flux<String> stringFlux = Flux.fromIterable(names).filter(s->s.startsWith("a")).map(s->s.toUpperCase()).log();

        StepVerifier.create(stringFlux).expectNext("ADAM","ANNA").verifyComplete();
    }

    @Test
    public void transformUsingLength(){
        Flux<Integer> stringFlux = Flux.fromIterable(names).map(s->s.length()).log();
        StepVerifier.create(stringFlux).expectNext(4,4,4,5).verifyComplete();
    }

    @Test
    public void transformUsingLength_Repeat(){
        Flux<Integer> stringFlux = Flux.fromIterable(names).map(s->s.length())
                .repeat(1)
                .log();
        StepVerifier.create(stringFlux).expectNext(4,4,4,5,4,4,4,5).verifyComplete();
    }


}
