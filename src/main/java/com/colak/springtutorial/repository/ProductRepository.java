package com.colak.springtutorial.repository;

import com.colak.springtutorial.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Stream<Product> findAllBy();

}