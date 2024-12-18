package com.example.service;

import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService {
    @Resource
    ProductMapper productMapper;
    /**
     * 获取所有商品
     *
     * @return 商品列表
     */
    public List<Product> getAllProducts() {
        return productMapper.selectAllProducts();
    }
    /**
     * 根据商品名获取商品详情
     *
     * @param name 商品名
     * @return 商品信息
     */
    public Product getProductByname(String name) {
        return productMapper.selectProductByname(name);
    }

    /**
     * 根据商品类别获取商品
     *
     * @param category 商品类别
     * @return 商品列表
     */
    public List<Product> getProductsByCategory(String category) {
        return productMapper.selectProductsByCategory(category);
    }

    public List<String>getAllCategories(){
        return productMapper.selectAllCategory();
    }
    public Product getProductById(Integer id) {
        return productMapper.selectProductById(id);
    }

}
