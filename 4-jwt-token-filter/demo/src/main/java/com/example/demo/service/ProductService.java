package com.example.demo.service;

import com.example.demo.model.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    public List<ProductEntity> getAllProducts();

    public ProductEntity getProductById(int id);

    public ProductEntity addProduct(ProductEntity product);

    public ProductEntity updateProduct(int id, ProductEntity product);

    public boolean deleteProductById(int id);
}
