package com.example.demo.service;

import com.example.demo.exception.ProductAlreadyExistsException;
import com.example.demo.exception.ProductDoesNotExistException;
import com.example.demo.model.ProductEntity;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductEntity getProductById(int id) {
        return productRepository.findById(id).orElseThrow(ProductDoesNotExistException::new);
    }

    @Override
    public ProductEntity addProduct(ProductEntity product) {
        if(productRepository.existsById(product.getId())) {
            throw new ProductAlreadyExistsException();
        }
        return productRepository.save(product);
    }

    @Override
    public ProductEntity updateProduct(int id, ProductEntity product) {
        return productRepository.findById(id).map(
                existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    return productRepository.save(existingProduct);
                }
        ).orElseThrow(ProductDoesNotExistException::new);
    }

    @Override
    public boolean deleteProductById(int id) {
        productRepository.findById(id).map(
                product -> {
                    productRepository.deleteById(id);
                    return product;
                }
        ).orElseThrow(ProductDoesNotExistException::new);
        return true;
    }
}
