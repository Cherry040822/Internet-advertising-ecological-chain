package com.example.controller;

import com.example.common.Result;
import com.example.entity.Product;
import com.example.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/selectall")
    public Result selectAll(){
        List<Product>list=productService.getAllProducts();
        return Result.success(list);
    }
    @GetMapping("/selectByname")
    public Result selectByName(String name){
        Product product=productService.getProductByname(name);
        return Result.success(product);
    }
    @GetMapping("/selectByCategory")
    public Result selectByCategory(String category){
        List<Product>list =productService.getProductsByCategory(category);
        return Result.success(list);
    }
    @GetMapping("/categories")
    public Result getCategories() {
       List<String> list =productService.getAllCategories();
       return Result.success(list);
    }
    @GetMapping("/selectById/{productId}")
    public Result selectById(@PathVariable("productId") Integer productId) {
        Product product = productService.getProductById(productId);
        return Result.success(product);
    }


}
