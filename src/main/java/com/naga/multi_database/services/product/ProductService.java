package com.naga.multi_database.services.product;


import com.naga.multi_database.mongodb.model.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Product product);

    List< Product > getAllProduct();

    Product getProductById(long productId);

    void deleteProduct(long id);
}

