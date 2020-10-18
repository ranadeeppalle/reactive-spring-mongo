package com.reactive.spring.repository;

import com.reactive.spring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item,String> {

}
