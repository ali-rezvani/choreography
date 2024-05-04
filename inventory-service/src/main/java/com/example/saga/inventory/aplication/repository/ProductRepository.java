package com.example.saga.inventory.aplication.repository;

import com.example.saga.inventory.aplication.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product,Integer> {
}
