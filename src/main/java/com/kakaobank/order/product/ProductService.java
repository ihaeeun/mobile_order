package com.kakaobank.order.product;

import com.kakaobank.order.common.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    public Product getProductDetail(long id) {
        return productRepository.findById(id).get();
    }

    public List<Product> updateStock(List<Product> products) {
        return this.productRepository.saveAll(products);
    }
}
