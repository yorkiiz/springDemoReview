package com.example.demo.user;

import com.example.demo.entity.iml.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public boolean deductStock(Long productId, Integer quantity) {
        int rows = productMapper.deductStock(productId, quantity);
        return rows > 0;
    }

    public boolean restoreStock(Long productId, Integer quantity) {
        int rows = productMapper.restoreStock(productId, quantity);
        return rows > 0;
    }
}
