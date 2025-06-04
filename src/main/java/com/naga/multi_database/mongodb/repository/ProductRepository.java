package com.naga.multi_database.mongodb.repository;

import com.naga.multi_database.mongodb.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends MongoRepository<Product, Long > {
}
