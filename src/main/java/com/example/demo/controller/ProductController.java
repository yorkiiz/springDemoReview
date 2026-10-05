package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Product;
import com.example.demo.entity.iml.ProductMapper;
import com.example.demo.user.ProductService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductService productService;

    public ProductController(ProductMapper productMapper, ProductService productService) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @GetMapping("/list")
    public Result<Page<Product>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        Page<Product> page = new Page<>(pageNum, pageSize);
        
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> 
                wrapper.like("name", keyword)
                       .or()
                       .like("description", keyword)
            );
        }
        
        if (minPrice != null) {
            queryWrapper.ge("price", minPrice);
        }
        
        if (maxPrice != null) {
            queryWrapper.le("price", maxPrice);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        Page<Product> result = productMapper.selectPage(page, queryWrapper);
        return Result.success(result);
    }

    @GetMapping("/detail/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.fail(404, "商品不存在");
        }
        return Result.success(product);
    }

    @PostMapping("/test/deduct")
    public Result<String> testDeduct(@RequestParam Long productId, @RequestParam Integer quantity) {
        boolean success = productService.deductStock(productId, quantity);
        if (success) {
            return Result.success("扣减成功");
        }
        return Result.fail("库存不足");
    }

    @PostMapping("/test/restore")
    public Result<String> testRestore(@RequestParam Long productId, @RequestParam Integer quantity) {
        boolean success = productService.restoreStock(productId, quantity);
        if (success) {
            return Result.success("恢复成功");
        }
        return Result.fail("恢复失败");
    }
}
