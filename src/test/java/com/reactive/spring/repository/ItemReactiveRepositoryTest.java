package com.reactive.spring.repository;

import com.reactive.spring.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;
    List<Item> itemsList = Arrays.asList(Item.builder().description("Samsung TV").price(400.00).build(),
            Item.builder().description("LG TV").price(300.00).build(),
            Item.builder().description("Apple TV").price(200.00).build(),
            Item.builder().description("Samsung Watch").price(100.00).build());
    @Before
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemsList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("saved " + item))
                .blockLast(); //never use it in actual code
    }

    @Test
    public void getAllItems(){
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}
